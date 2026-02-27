package examples;

import org.jcodec.api.awt.AWTSequenceEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class N19_ParisTrajectoryStaticMaps {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int FPS = 7;  // Reduced from 10 to 7 to slow down
    private static final int TILE_SIZE = 256;
    private static final String OUTPUT_VIDEO = "paris_trajectory.mov";

    // Zoom will be calculated to see entire trajectory
    private static int ZOOM_LEVEL = 11; // Placeholder value

    // Map center (calculated to encompass entire trajectory)
    private static double CENTER_LAT;
    private static double CENTER_LON;

    public static void main(String[] args) {
        try {
            System.out.println("Animation: Paris Tour");

            // Define trajectory
            List<Coordinate> trajectory = getParisPerimeterCoordinates();
            System.out.printf("%d trajectory points\n", trajectory.size());

            // Calculate optimal center and zoom
            calculateOptimalView(trajectory);
            System.out.printf("Center: %.4f°N, %.4f°E\n", CENTER_LAT, CENTER_LON);
            System.out.printf("Zoom: %d\n\n", ZOOM_LEVEL);

            // Generate video
            System.out.println("Generating video...");
            generateVideoFixed(trajectory);

            System.out.println("\nAnimation created: " + OUTPUT_VIDEO);
            System.out.println("   Duration: " + (trajectory.size() / (double) FPS) + " seconds");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculate optimal center and zoom to see entire trajectory
     */
    private static void calculateOptimalView(List<Coordinate> trajectory) {
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;

        for (Coordinate c : trajectory) {
            minLat = Math.min(minLat, c.lat);
            maxLat = Math.max(maxLat, c.lat);
            minLon = Math.min(minLon, c.lon);
            maxLon = Math.max(maxLon, c.lon);
        }

        // Trajectory center
        CENTER_LAT = (minLat + maxLat) / 2.0;
        CENTER_LON = (minLon + maxLon) / 2.0;

        // Calculate optimal zoom (with margin)
        double latRange = maxLat - minLat;
        double lonRange = maxLon - minLon;
        double maxRange = Math.max(latRange, lonRange) * 1.2; // 20% margin

        if (maxRange > 5.0) ZOOM_LEVEL = 8;
        else if (maxRange > 2.0) ZOOM_LEVEL = 9;
        else if (maxRange > 1.0) ZOOM_LEVEL = 10;
        else if (maxRange > 0.5) ZOOM_LEVEL = 11;
        else if (maxRange > 0.2) ZOOM_LEVEL = 12;
        else ZOOM_LEVEL = 13;
    }

    /**
     * Generate video with a fixed map and moving points
     */
    private static void generateVideoFixed(List<Coordinate> trajectory) throws Exception {
        File output = new File(OUTPUT_VIDEO);
        AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(output, FPS);

        System.out.println("Downloading base map...");
        BufferedImage baseMap = createFixedMap();
        System.out.println("Map loaded\n");

        int totalFrames = trajectory.size();
        int trailLength = 15; // Number of past blue positions to display

        try {
            for (int i = 0; i < totalFrames; i++) {
                // Create a copy of base map to draw upon
                BufferedImage frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = frame.createGraphics();

                // Draw base map
                g2d.drawImage(baseMap, 0, 0, null);

                // Enable antialiasing to smooth the sharp edges
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw trail (previous points)
                int startTrail = Math.max(0, i - trailLength);
                for (int j = startTrail; j < i; j++) {
                    java.awt.Point p = coordinateToPixel(trajectory.get(j));

                    // Transparency gradient (older = more transparent)
                    float alpha = 0.4f + (0.6f * (j - startTrail) / (float) trailLength);
                    g2d.setColor(new Color(30, 100, 200, (int)(alpha * 255)));

                    // Decreasing size: the older the position is, the smaller and transparent it becomes
                    int size = 6 + (int)((j - startTrail) * 4.0 / trailLength);
                    g2d.fillOval(p.x - size/2, p.y - size/2, size, size);
                }

                // Draw trajectory line (to see path connecting the past positions better)
                g2d.setColor(new Color(30, 100, 200, 150));
                g2d.setStroke(new BasicStroke(2));
                for (int j = startTrail; j < i; j++) {
                    java.awt.Point p1 = coordinateToPixel(trajectory.get(j));
                    java.awt.Point p2 = coordinateToPixel(trajectory.get(j + 1));
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }

                // Draw current point (RED, larger)
                java.awt.Point current = coordinateToPixel(trajectory.get(i));

                // Red point
                g2d.setColor(Color.RED);
                g2d.fillOval(current.x - 10, current.y - 10, 20, 20);

                // Dark red border
                g2d.setColor(new Color(139, 0, 0));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(current.x - 10, current.y - 10, 20, 20);

                // Overlay with information
                drawOverlay(g2d, i, totalFrames, trajectory.get(i));

                g2d.dispose();

                // Encode
                encoder.encodeImage(frame);

                // Progress
                if ((i + 1) % 5 == 0 || i == totalFrames - 1) {
                    int progress = (int) (((i + 1) / (double) totalFrames) * 100);
                    System.out.printf("   Frame %d/%d (%d%%)\n", i + 1, totalFrames, progress);
                }
            }

            System.out.println("All frames encoded");

        } finally {
            encoder.finish();
        }
    }

    /**
     * Create base map
     */
    private static BufferedImage createFixedMap() throws Exception {
        int centerTileX = lon2tile(CENTER_LON, ZOOM_LEVEL);
        int centerTileY = lat2tile(CENTER_LAT, ZOOM_LEVEL);

        // Number of tiles to load
        int tilesX = (WIDTH / TILE_SIZE) + 2;
        int tilesY = (HEIGHT / TILE_SIZE) + 2;

        BufferedImage mapImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = mapImage.createGraphics();

        // Light gray background
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Download and assemble tiles
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
                        System.err.println("Tile error " + tx + "," + ty);
                    }
                }
            }
        }

        g.dispose();
        return mapImage;
    }

    /**
     * Convert geographic coordinate to pixel position on map
     */
    private static java.awt.Point coordinateToPixel(Coordinate coord) {
        // Calculate position in "tile coordinates"
        double scale = Math.pow(2, ZOOM_LEVEL);

        double centerX = (CENTER_LON + 180.0) / 360.0 * scale;
        double centerY = (1.0 - Math.log(Math.tan(Math.toRadians(CENTER_LAT)) +
                1.0 / Math.cos(Math.toRadians(CENTER_LAT))) / Math.PI) / 2.0 * scale;

        double pointX = (coord.lon + 180.0) / 360.0 * scale;
        double pointY = (1.0 - Math.log(Math.tan(Math.toRadians(coord.lat)) +
                1.0 / Math.cos(Math.toRadians(coord.lat))) / Math.PI) / 2.0 * scale;

        // Convert to pixels
        int px = WIDTH / 2 + (int)((pointX - centerX) * TILE_SIZE);
        int py = HEIGHT / 2 + (int)((pointY - centerY) * TILE_SIZE);

        return new java.awt.Point(px, py);
    }

    /**
     * Download OSM tile
     */
    private static BufferedImage downloadTile(int zoom, int x, int y) throws Exception {
        String url = String.format("https://tile.openstreetmap.org/%d/%d/%d.png", zoom, x, y);
        URL tileUrl = new URL(url);
        java.net.URLConnection connection = tileUrl.openConnection();
        // It is necessary to set your User-Agent
        // You'll otherwise be blocked by a 403 HTTP Error
        connection.setRequestProperty("User-Agent", "ParisTrajectoryFixed/1.0");
        return ImageIO.read(connection.getInputStream());
    }

    private static int lon2tile(double lon, int zoom) {
        return (int) Math.floor((lon + 180.0) / 360.0 * Math.pow(2.0, zoom));
    }

    private static int lat2tile(double lat, int zoom) {
        return (int) Math.floor((1.0 - Math.log(Math.tan(Math.toRadians(lat)) +
                1.0 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2.0 * Math.pow(2.0, zoom));
    }

    /**
     * Draw overlay with title and current point information
     */
    private static void drawOverlay(Graphics2D g2d, int frameNum, int totalFrames, Coordinate currentPoint) {
        // Main frame (top left)
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(20, 20, 450, 80, 15, 15); // Width increased from 350 to 450

        // Title text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Tour of Paris", 40, 50);

        // Progress text
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        int progress = (int) (((frameNum + 1) / (double) totalFrames) * 100);
        g2d.drawString(String.format("Progress: %d / %d (%d%%)",
                frameNum + 1, totalFrames, progress), 40, 80);

        // Current point information frame (bottom right)
        int infoWidth = 320;
        int infoHeight = 90;
        int infoX = WIDTH - infoWidth - 20;
        int infoY = HEIGHT - infoHeight - 20;

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(infoX, infoY, infoWidth, infoHeight, 15, 15);

        // Info frame content
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Current location", infoX + 15, infoY + 25);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2d.drawString(String.format("Latitude  : %.6f°N", currentPoint.lat), infoX + 15, infoY + 50);
        g2d.drawString(String.format("Longitude : %.6f°E", currentPoint.lon), infoX + 15, infoY + 70);

        // Note: Timestamp can be displayed when available in data
        // g2d.drawString(String.format("Time: %s", timestamp), infoX + 15, infoY + 90);
    }

    /**
     * Paris ring road coordinates
     */
    private static List<Coordinate> getParisPerimeterCoordinates() {
        List<Coordinate> coords = new ArrayList<>();

        coords.add(new Coordinate(48.8975, 2.3617));
        coords.add(new Coordinate(48.8989, 2.3524));
        coords.add(new Coordinate(48.8994, 2.3441));
        coords.add(new Coordinate(48.8975, 2.3285));
        coords.add(new Coordinate(48.8935, 2.3154));
        coords.add(new Coordinate(48.8866, 2.2965));
        coords.add(new Coordinate(48.8788, 2.2825));
        coords.add(new Coordinate(48.8731, 2.2756));
        coords.add(new Coordinate(48.8650, 2.2705));
        coords.add(new Coordinate(48.8564, 2.2715));
        coords.add(new Coordinate(48.8480, 2.2755));
        coords.add(new Coordinate(48.8397, 2.2821));
        coords.add(new Coordinate(48.8325, 2.2898));
        coords.add(new Coordinate(48.8274, 2.3047));
        coords.add(new Coordinate(48.8235, 2.3156));
        coords.add(new Coordinate(48.8200, 2.3290));
        coords.add(new Coordinate(48.8189, 2.3425));
        coords.add(new Coordinate(48.8199, 2.3566));
        coords.add(new Coordinate(48.8220, 2.3688));
        coords.add(new Coordinate(48.8258, 2.3798));
        coords.add(new Coordinate(48.8305, 2.3915));
        coords.add(new Coordinate(48.8360, 2.4015));
        coords.add(new Coordinate(48.8431, 2.4085));
        coords.add(new Coordinate(48.8512, 2.4105));
        coords.add(new Coordinate(48.8595, 2.4089));
        coords.add(new Coordinate(48.8675, 2.4045));
        coords.add(new Coordinate(48.8752, 2.3965));
        coords.add(new Coordinate(48.8825, 2.3875));
        coords.add(new Coordinate(48.8878, 2.3795));
        coords.add(new Coordinate(48.8922, 2.3705));
        coords.add(new Coordinate(48.8950, 2.3655));
        coords.add(new Coordinate(48.8975, 2.3617));

        return coords;
    }

    static class Coordinate {
        double lat, lon;
        Coordinate(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}