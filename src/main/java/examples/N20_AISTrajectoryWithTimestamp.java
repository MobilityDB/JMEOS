package examples;

import functions.error_handler;
import functions.error_handler_fn;
import org.jcodec.api.awt.AWTSequenceEncoder;
import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import org.locationtech.jts.geom.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import utils.ConversionUtils;
import static functions.functions.*;

public class N20_AISTrajectoryWithTimestamp {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS = 5;
    private static final int TILE_SIZE = 256;

    private static int ZOOM_LEVEL = 11;
    private static double CENTER_LAT;
    private static double CENTER_LON;

    public static void main(String[] args) {
        try {
            System.out.println("AIS Trajectory Generator");
            System.out.println("=========================================\n");

            // Initialize MEOS
            meos_initialize_timezone("UTC");
            error_handler_fn error_handler = new error_handler();
            meos_initialize_error_handler(error_handler);

            // Extract from MobilityDB
            long mmsi = 265513270;
            System.out.printf("Extracting trajectory for MMSI: %d\n\n", mmsi);

            List<CoordinateWithTime> trajectory = getTrajectoryFromMobilityDB(mmsi);

            if (trajectory.isEmpty()) {
                System.err.println("No trajectory extracted!");
                return;
            }

            System.out.printf("\n%d points extracted\n", trajectory.size());

            // Display first 5 points
            System.out.println("\nFirst points (for verification):");
            for (int i = 0; i < Math.min(5, trajectory.size()); i++) {
                CoordinateWithTime c = trajectory.get(i);
                System.out.printf("  [%d] Lat: %.6f, Lon: %.6f, Time: %s\n",
                        i, c.lat, c.lon, c.timestamp);
            }

            System.out.printf("   Period: %s -> %s\n\n",
                    trajectory.get(0).timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    trajectory.get(trajectory.size()-1).timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            // Compute optimal view
            calculateOptimalView(trajectory);
            System.out.printf("Map center: %.4f°N, %.4f°E\n", CENTER_LAT, CENTER_LON);
            System.out.printf("Zoom: %d\n\n", ZOOM_LEVEL);

            // Generate video
            String outputFile = "ais_trajectory.mov";
            System.out.println("Generating video: " + outputFile);
            generateVideo(trajectory, outputFile);

            System.out.println("\nCompleted: " + outputFile);

            meos_finalize();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Extract trajectory from MobilityDB
     * Uses MEOS functions
     */
    public static List<CoordinateWithTime> getTrajectoryFromMobilityDB(long mmsi)
            throws Exception {

        String jdbcUrl = "jdbc:postgresql://host.docker.internal:5432/postgres?user=postgres&password=postgres";
        Connection conn = DriverManager.getConnection(jdbcUrl);

        String sql = "SELECT trip::text FROM public.AISTrips WHERE MMSI = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setLong(1, mmsi);
        ResultSet rs = stmt.executeQuery();

        List<CoordinateWithTime> coords = new ArrayList<>();

        if (rs.next()) {
            String wkt = rs.getString(1);
            System.out.println("WKT received (first 200 chars): " + wkt.substring(0, Math.min(200, wkt.length())));

            // Parse with MEOS
            Pointer tempPtr = tgeogpoint_in(wkt);

            // Get all instants
            Runtime runtime = Runtime.getSystemRuntime();
            Pointer countPointer = Memory.allocate(runtime, 4);

            Pointer instantsArray = temporal_instants(tempPtr, countPointer);
            int numInstants = countPointer.getInt(0);

            System.out.printf("Number of instants: %d\n", numInstants);

            for (int i = 0; i < numInstants; i++) {
                // Get pointer to instant
                Pointer instantPtr = instantsArray.getPointer((long) i * Long.BYTES);

                // Extract timestamp
                // Use temporal_start_timestamptz which directly returns OffsetDateTime
                OffsetDateTime timestamp = temporal_start_timestamptz(instantPtr);

                // Extract geographic value
                Pointer geoPtr = tgeo_start_value(instantPtr);

                // Convert GSERIALIZED to JTS Point
                Point point = ConversionUtils.gserialized_to_shapely_point(geoPtr, 15);

                // Extract lat/lon with getters
                double lat = point.getY();  // Latitude
                double lon = point.getX();  // Longitude

                coords.add(new CoordinateWithTime(lat, lon, timestamp));

                // Debug every 100 points
                if ((i + 1) % 100 == 0 || i == 0) {
                    System.out.printf("  Point %d: Lat=%.6f, Lon=%.6f, Time=%s\n",
                            i, lat, lon, timestamp);
                }
            }
        } else {
            System.err.println("No result for MMSI " + mmsi);
        }

        stmt.close();
        conn.close();

        return coords;
    }

    /**
     * Calculate optimal view
     */
    private static void calculateOptimalView(List<CoordinateWithTime> trajectory) {
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;

        for (CoordinateWithTime c : trajectory) {
            minLat = Math.min(minLat, c.lat);
            maxLat = Math.max(maxLat, c.lat);
            minLon = Math.min(minLon, c.lon);
            maxLon = Math.max(maxLon, c.lon);
        }

        CENTER_LAT = (minLat + maxLat) / 2.0;
        CENTER_LON = (minLon + maxLon) / 2.0;

        double latRange = maxLat - minLat;
        double lonRange = maxLon - minLon;
        double maxRange = Math.max(latRange, lonRange) * 1.2;

        System.out.printf("Geographic extent:\n");
        System.out.printf("   Lat: %.6f° to %.6f° (Delta=%.6f°, ~%.0f m)\n",
                minLat, maxLat, latRange, latRange * 111000);
        System.out.printf("   Lon: %.6f° to %.6f° (Delta=%.6f°, ~%.0f m)\n",
                minLon, maxLon, lonRange, lonRange * 111000 * Math.cos(Math.toRadians(CENTER_LAT)));

        // Adaptive zoom
        if (maxRange > 5.0) ZOOM_LEVEL = 8;
        else if (maxRange > 2.0) ZOOM_LEVEL = 9;
        else if (maxRange > 1.0) ZOOM_LEVEL = 10;
        else if (maxRange > 0.5) ZOOM_LEVEL = 11;
        else if (maxRange > 0.2) ZOOM_LEVEL = 12;
        else if (maxRange > 0.1) ZOOM_LEVEL = 13;
        else if (maxRange > 0.05) ZOOM_LEVEL = 14;
        else if (maxRange > 0.02) ZOOM_LEVEL = 15;
        else if (maxRange > 0.01) ZOOM_LEVEL = 16;
        else if (maxRange > 0.005) ZOOM_LEVEL = 17;
        else if (maxRange > 0.002) ZOOM_LEVEL = 18;
        else ZOOM_LEVEL = 19;  // Maximum zoom for docked vessels
    }

    /**
     * Generate video
     */
    private static void generateVideo(List<CoordinateWithTime> trajectory, String outputFile)
            throws Exception {

        File output = new File(outputFile);
        AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(output, FPS);

        System.out.println("Loading map...");
        BufferedImage baseMap = createFixedMap();
        System.out.println("Map loaded\n");

        int totalFrames = trajectory.size();
        int trailLength = 15;

        // Check pixel positions of first points
        System.out.println("Pixel positions of first 3 points:");
        for (int i = 0; i < Math.min(3, trajectory.size()); i++) {
            java.awt.Point p = coordinateToPixel(trajectory.get(i));
            System.out.printf("  Point %d: pixel(%d, %d)\n", i, p.x, p.y);
        }
        System.out.println();

        try {
            for (int i = 0; i < totalFrames; i++) {
                BufferedImage frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = frame.createGraphics();

                g2d.drawImage(baseMap, 0, 0, null);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Blue Trail for past positions
                int startTrail = Math.max(0, i - trailLength);

                // DEBUG first frame
                if (i == 0) {
                    System.out.printf("Frame 0: startTrail=%d, i=%d, trail points=%d\n",
                            startTrail, i, i - startTrail);
                }

                for (int j = startTrail; j < i; j++) {
                    java.awt.Point p = coordinateToPixel(trajectory.get(j));
                    float alpha = 0.4f + (0.6f * (j - startTrail) / (float) trailLength);
                    g2d.setColor(new Color(30, 100, 200, (int)(alpha * 255)));
                    int size = 6 + (int)((j - startTrail) * 4.0 / trailLength);
                    g2d.fillOval(p.x - size/2, p.y - size/2, size, size);
                }

                // Trajectory line
                g2d.setColor(new Color(30, 100, 200, 150));
                g2d.setStroke(new BasicStroke(2));
                for (int j = startTrail; j < i; j++) {
                    java.awt.Point p1 = coordinateToPixel(trajectory.get(j));
                    java.awt.Point p2 = coordinateToPixel(trajectory.get(j + 1));
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }

                // Current point (RED)
                java.awt.Point current = coordinateToPixel(trajectory.get(i));

                // DEBUG: Display position every 500 points
                if ((i + 1) % 500 == 0) {
                    System.out.printf("Frame %d: pixel position(%d, %d)\n", i, current.x, current.y);
                }

                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillOval(current.x - 12, current.y - 12, 24, 24);
                g2d.setColor(Color.RED);
                g2d.fillOval(current.x - 10, current.y - 10, 20, 20);
                g2d.setColor(new Color(139, 0, 0));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(current.x - 10, current.y - 10, 20, 20);

                // Overlay with timestamp
                drawOverlayWithTimestamp(g2d, i, totalFrames, trajectory.get(i));

                g2d.dispose();
                encoder.encodeImage(frame);

                if ((i + 1) % 500 == 0 || i == totalFrames - 1) {
                    int progress = (int) (((i + 1) / (double) totalFrames) * 100);
                    System.out.printf("   Frame %d/%d (%d%%)\n", i + 1, totalFrames, progress);
                }
            }
        } finally {
            encoder.finish();
        }
    }

    /**
     * Overlay with timestamp
     */
    private static void drawOverlayWithTimestamp(Graphics2D g2d, int frameNum, int totalFrames,
                                                 CoordinateWithTime point) {
        // Main frame
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(20, 20, 450, 80, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("AIS Trajectory", 40, 50);

        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        int progress = (int) (((frameNum + 1) / (double) totalFrames) * 100);
        g2d.drawString(String.format("Progress: %d / %d (%d%%)",
                frameNum + 1, totalFrames, progress), 40, 80);

        // Information frame
        int infoWidth = 340;
        int infoHeight = 110;
        int infoX = WIDTH - infoWidth - 20;
        int infoY = HEIGHT - infoHeight - 20;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(infoX, infoY, infoWidth, infoHeight, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Current position", infoX + 15, infoY + 25);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2d.drawString(String.format("Latitude  : %.6f°N", point.lat), infoX + 15, infoY + 50);
        g2d.drawString(String.format("Longitude : %.6f°E", point.lon), infoX + 15, infoY + 70);

        // Timestamp - Standard ISO format
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timeStr = point.timestamp.format(formatter);
            g2d.drawString(String.format("Time      : %s", timeStr), infoX + 15, infoY + 90);
        } catch (Exception e) {
            // If format error, display raw timestamp
            g2d.drawString(String.format("Time      : %s", point.timestamp.toString()),
                    infoX + 15, infoY + 90);
        }
    }

    private static BufferedImage createFixedMap() throws Exception {
        int centerTileX = lon2tile(CENTER_LON, ZOOM_LEVEL);
        int centerTileY = lat2tile(CENTER_LAT, ZOOM_LEVEL);
        int tilesX = (WIDTH / TILE_SIZE) + 2;
        int tilesY = (HEIGHT / TILE_SIZE) + 2;

        BufferedImage mapImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = mapImage.createGraphics();
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        int startTileX = centerTileX - tilesX / 2;
        int startTileY = centerTileY - tilesY / 2;

        for (int x = 0; x <= tilesX; x++) {
            for (int y = 0; y <= tilesY; y++) {
                int tx = startTileX + x;
                int ty = startTileY + y;
                if (tx >= 0 && ty >= 0 && tx < Math.pow(2, ZOOM_LEVEL) && ty < Math.pow(2, ZOOM_LEVEL)) {
                    try {
                        BufferedImage tile = downloadTile(ZOOM_LEVEL, tx, ty);
                        if (tile != null) {
                            int px = (x * TILE_SIZE) - (TILE_SIZE / 2);
                            int py = (y * TILE_SIZE) - (TILE_SIZE / 2);
                            g.drawImage(tile, px, py, null);
                        }
                    } catch (Exception e) {
                        System.err.println("Error while downloading tile: " + e.getMessage());
                        System.out.println("startTileX: " + tx + " | startTileY: " + ty);
                        System.out.println("tx: " + tx + " | ty: " + ty);
                        e.printStackTrace();
                    }
                }
            }
        }
        g.dispose();
        return mapImage;
    }

    private static java.awt.Point coordinateToPixel(CoordinateWithTime coord) {
        double scale = Math.pow(2, ZOOM_LEVEL);
        double centerX = (CENTER_LON + 180.0) / 360.0 * scale;
        double centerY = (1.0 - Math.log(Math.tan(Math.toRadians(CENTER_LAT)) +
                1.0 / Math.cos(Math.toRadians(CENTER_LAT))) / Math.PI) / 2.0 * scale;
        double pointX = (coord.lon + 180.0) / 360.0 * scale;
        double pointY = (1.0 - Math.log(Math.tan(Math.toRadians(coord.lat)) +
                1.0 / Math.cos(Math.toRadians(coord.lat))) / Math.PI) / 2.0 * scale;

        return new java.awt.Point(
                WIDTH / 2 + (int)((pointX - centerX) * TILE_SIZE),
                HEIGHT / 2 + (int)((pointY - centerY) * TILE_SIZE)
        );
    }

    private static BufferedImage downloadTile(int zoom, int x, int y) throws Exception {
        String url = String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, x, y);
        URL tileUrl = new URL(url);
        java.net.URLConnection conn = tileUrl.openConnection();
        conn.setRequestProperty("User-Agent", "AISTrajectoryDebug/1.0");
        Thread.sleep(50);
        return ImageIO.read(conn.getInputStream());
    }

    private static int lon2tile(double lon, int zoom) {
        return (int) Math.floor((lon + 180.0) / 360.0 * Math.pow(2.0, zoom));
    }

    private static int lat2tile(double lat, int zoom) {
        return (int) Math.floor((1.0 - Math.log(Math.tan(Math.toRadians(lat)) +
                1.0 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2.0 * Math.pow(2.0, zoom));
    }

    /**
     * Class to store coordinates + timestamp
     */
    static class CoordinateWithTime {
        double lat, lon;
        OffsetDateTime timestamp;

        CoordinateWithTime(double lat, double lon, OffsetDateTime timestamp) {
            this.lat = lat;
            this.lon = lon;
            this.timestamp = timestamp;
        }
    }
}