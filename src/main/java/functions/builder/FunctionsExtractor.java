package functions.builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.BuilderLibrary.extractPatternFromFile;
import static utils.BuilderLibrary.writeFileFromArray;

/**
 * Class used to extract the functions from the MEOS library.
 * Run with ./script folder
 *
 * @author Killian Monnier
 * @since 27/06/2023
 */
public class FunctionsExtractor {
	private static final String JMEOS_PATH = "src/main/java/";
	private static final String INPUT_FILE_PATH = "src/libs/meos.h";
	private static final String OUTPUT_FUNCTIONS_PATH = JMEOS_PATH + "functions/builder/tmp/meos_functions.h";
	private static final String OUTPUT_TYPES_PATH = JMEOS_PATH + "functions/builder/tmp/meos_types.h";
	
	/**
	 * RegEx model for recognizing a function in the meos.h file
	 */
	private static final String FUNCTION_PATTERN = "extern (static inline )?[a-zA-Z0-9_]+ [a-zA-Z0-9_*]+\\([a-zA-Z0-9_* ,]+\\);";
	
	/**
	 * Type recognition RegEx pattern in meos.h file
	 */
	private static final String TYPES_PATTERN = "typedef\\s(?!struct|enum)\\w+\\s\\w+;";
	
	/**
	 * Script launch function.
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		// Retrieve the value of the JMEOS_PATH environment variable
		String jmeosHome = System.getenv("JMEOS_HOME");
		
		// Check if environment variable exists
		if (jmeosHome != null) {
			System.out.println("JMEOS_HOME: " + jmeosHome);
			
			// Set the current working directory
			System.setProperty("user.dir", jmeosHome);
			
			// Check if the current working directory has been updated
			String currentWorkingDir = System.getProperty("user.dir");
			System.out.println("Current working directory: " + currentWorkingDir);
			
			ArrayList<String> functions = extractPatternFromFile(INPUT_FILE_PATH, FUNCTION_PATTERN);
			writeFileFromArray(functions, OUTPUT_FUNCTIONS_PATH);
			System.out.println("Feature extraction completed. The functions have been written to the file " + OUTPUT_FUNCTIONS_PATH);
			ArrayList<String> types = getTypesFromFile();
			writeFileFromArray(types, OUTPUT_TYPES_PATH);
			System.out.println("Extraction of types completed. The types have been written to the file " + OUTPUT_TYPES_PATH);
		} else {
			System.err.println("The JMEOS_HOME environment variable is not set.\nCancellation of extraction.");
		}
	}
	
	/**
	 * Get types from a file.
	 *
	 * @return the list of lines
	 */
	private static ArrayList<String> getTypesFromFile() {
		ArrayList<String> rawTypes = extractPatternFromFile(FunctionsExtractor.INPUT_FILE_PATH, FunctionsExtractor.TYPES_PATTERN);
		ArrayList<String> structureNames = getStructureNames(FunctionsExtractor.INPUT_FILE_PATH);
		ArrayList<String> filteredTypes = new ArrayList<>();
		
		// Add typedefs if they are not structure type.
		for (String rawType : rawTypes) {
			String[] words = rawType.trim().split("\\s+");
			if (words.length >= 2) {
				String typeName = words[1];
				if (!structureNames.contains(typeName)) {
					filteredTypes.add(rawType);
				}
			}
		}
		
		return filteredTypes;
	}
	
	/**
	 * Retrieves structure names from file.
	 *
	 * @param filePath file path
	 * @return list of structure names
	 */
	public static ArrayList<String> getStructureNames(String filePath) {
		List<String> structureNames = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
			
			String regex = "typedef\\s+struct(\\s\\w+)?\\s*\\{[\\s\\S]*?}\\s*(\\w+)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content.toString());
			
			while (matcher.find()) {
				String structureName = matcher.group(2);
				structureNames.add(structureName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(structureNames);
	}
}
