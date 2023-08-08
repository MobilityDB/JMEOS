package utils;

import jnr.ffi.LibraryLoader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * <a href="https://github.com/jnr/jnr-ffi/issues/93#issuecomment-717760847">...</a>
 *
 * @param <T> Library defined interface
 */
public class JarLibraryLoader<T> {
	
	private final Class<T> libraryClass;
	private final String libraryName;
	private final String libraryFileName;
	private final String libraryPath;
	private final String projectPath = System.getProperty("user.dir");
	
	/**
	 * Constructor of this class.
	 * @param libraryClass    class of the library defined interface
	 * @param libraryName     name of the library
	 * @param libraryFileName filename of the library
	 * @param libraryPath     path of the library file
	 */
	public JarLibraryLoader(Class<T> libraryClass, String libraryName, String libraryFileName, String libraryPath) {
		this.libraryClass = libraryClass;
		this.libraryName = libraryName;
		this.libraryFileName = libraryFileName;
		this.libraryPath = libraryPath;
	}
	
	/**
	 * Unpack library from jar and copy it to temp file
	 *
	 * @return Return temp file with copy of library
	 * @throws IOException exception during process
	 */
	public File createTempFileFromJar() throws IOException {
		// Copy the native library from the JAR to a temporary location
		System.out.println(libraryPath + "/" + libraryFileName);
		URL url = JarLibraryLoader.class.getResource(libraryPath + "/" + libraryFileName);
		System.out.println(url.getFile());
		try {
			InputStream is = JarLibraryLoader.class.getResourceAsStream(libraryPath + "/" + libraryFileName);
			if (is != null) {
				File tempFile = File.createTempFile(getPrefix(libraryFileName), "." + getSuffix(libraryFileName));
				tempFile.deleteOnExit(); // The temp file will be deleted when the JVM exits
				
				try (FileOutputStream fos = new FileOutputStream(tempFile)) {
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1) {
						fos.write(buffer, 0, bytesRead);
					}
				}
				
				try {
					copyFileFromJar("/jmeos/lib/libmeos.so", projectPath + "/src/lib/libmeos.so");
					System.out.println("File copied successfully.");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				if (tempFile.exists()) {
					System.out.println("File Absolute Path: " + tempFile.getAbsolutePath());
					System.out.println("File Size: " + getFileSizeInMB(tempFile) + " MB");
					return tempFile;
				}
			} else {
				throw new IOException("Library not found in the JAR.");
			}
		} catch (IOException e) {
			throw new IOException("Error copying library: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Return the file size in MB.
	 *
	 * @param file the file
	 * @return the file size
	 */
	public static double getFileSizeInMB(File file) {
		try {
			// Get the file size in bytes
			long fileSizeBytes = file.length();
			
			// Convert bytes to megabytes (MB) using floating-point division
			return ((double) fileSizeBytes / (1024 * 1024));
		} catch (Exception e) {
			System.err.println("Error while getting file size: " + e.getMessage());
		}
		return 0;
	}
	
	/**
	 * Return the prefix of a file.
	 *
	 * @param filename name of the file
	 * @return file prefix
	 */
	public static String getPrefix(String filename) {
		int lastDotIndex = filename.lastIndexOf('.');
		if (lastDotIndex != -1) {
			return filename.substring(0, lastDotIndex);
		}
		return filename;
	}
	
	/**
	 * Return the suffix of a file.
	 *
	 * @param filename name of the file
	 * @return file suffix
	 */
	public static String getSuffix(String filename) {
		int lastDotIndex = filename.lastIndexOf('.');
		if (lastDotIndex != -1) {
			return filename.substring(lastDotIndex + 1);
		}
		return "";
	}
	
	/**
	 * Provide the library instance.
	 *
	 * @return the library instance
	 */
	public T getLibraryInstance() {
		File libraryFile = null;
		try {
			libraryFile = createTempFileFromJar();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		assert libraryFile != null;
		return LibraryLoader.create(libraryClass).search(projectPath + "/src/lib").load(libraryName);
	}
	
	public static void copyFileFromJar(String resourcePath, String destinationPath) throws IOException {
		// Créer le dossier parent s'il n'existe pas
		File destinationFile = new File(destinationPath);
		File parentDirectory = destinationFile.getParentFile();
		if (!parentDirectory.exists()) {
			parentDirectory.mkdirs();
		}
		
		URL resourceUrl = JarLibraryLoader.class.getResource(resourcePath);
		if (resourceUrl == null) {
			throw new FileNotFoundException("Resource not found: " + resourcePath);
		}
		
		try (InputStream inputStream = resourceUrl.openStream();
			 OutputStream outputStream = new FileOutputStream(destinationPath)) {
			
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
		}
	}
	
	
	public static String getOSName() {
		String osName = System.getProperty("os.name").toLowerCase();
		System.out.println("osName: " + osName);
		
		if (osName.contains("win")) {
			return "Windows";
		} else if (osName.contains("mac")) {
			return "macOS";
		} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			return "Linux";
		} else if (osName.contains("sunos")) {
			return "Solaris";
		} else {
			return "Unknown";
		}
	}
	
}