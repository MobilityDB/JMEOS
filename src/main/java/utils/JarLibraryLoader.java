package utils;

import jnr.ffi.LibraryLoader;

import java.io.*;
import java.net.URL;

/**
 * Utility library to permit the loading of a library in a JAR.
 * <a href="https://github.com/jnr/jnr-ffi/issues/93#issuecomment-717760847">...</a>
 *
 * @param <T> Library defined interface
 * @author Killian Monier and Nidhal Mareghni
 * @since 13/08/2023
 */
public class JarLibraryLoader<T> {
	
	private final Class<T> libraryClass;
	private final String libraryName;
	private final String projectPath = System.getProperty("user.dir");
	
	/**
	 * Constructor of this class.
	 *
	 * @param libraryClass Class of the library defined interface
	 * @param libraryName  Name of the library
	 */
	public JarLibraryLoader(Class<T> libraryClass, String libraryName) {
		this.libraryClass = libraryClass;
		this.libraryName = libraryName;
	}
	
	/**
	 * Create a new instance of {@link JarLibraryLoader}
	 *
	 * @param libraryClass Class of the library defined interface
	 * @param libraryName  Name of the library
	 * @param <T>          Library defined interface
	 * @return New instance of JarLibraryLoader
	 */
	public static <T> JarLibraryLoader<T> create(Class<T> libraryClass, String libraryName) {
		return new JarLibraryLoader<>(libraryClass, libraryName);
	}
	
	/**
	 * Copies a file from the JAR resources to a specified destination on the local file system.
	 *
	 * @param resourcePath    The path of the file within the JAR resources to copy.
	 * @param destinationPath The path of the destination where the file will be copied to.
	 * @throws IOException           If an I/O error occurs during the copying process.
	 * @throws FileNotFoundException If the specified resource path cannot be found in the JAR resources.
	 */
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
	
	/**
	 * Retrieves the name of the operating system on which the Java program is running.
	 *
	 * @return The name of the operating system (e.g., "Windows", "Linux", "macOS").
	 */
	public static String getOSName() {
		String osName = System.getProperty("os.name").toLowerCase();
		
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
	
	/**
	 * Provide the library instance.
	 *
	 * @return The library instance
	 */
	public T getLibraryInstance() {
		if (getOSName().equals("Linux")) {
			try {
				copyFileFromJar("/jmeos/lib/libmeos.so", projectPath + "/src/lib/libmeos.so");
				System.out.println("File copied successfully.");
				
				return LibraryLoader.create(libraryClass).search(projectPath + "/src/lib").load(libraryName);
			} catch (IOException e) {
				System.out.println("Running in test mode");
				return LibraryLoader.create(libraryClass).search(projectPath + "/src/lib").load(libraryName);
			}
		} else {
			throw new UnsupportedOperationException("JMEOS is only supported on Linux OS");
		}
	}
}