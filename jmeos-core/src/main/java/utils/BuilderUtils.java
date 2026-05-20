package utils;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java class for manipulating StringBuilders as well as strings to allow string modification, reading and file writing operations.
 *
 * @author Nidhal Mareghni and Killian Monnier
 * @since 27/07/2023
 */
public class BuilderUtils {
	
	/**
	 * Writes lines to a file.
	 *
	 * @param lines    the list of extracted rows
	 * @param filePath the file path
	 */
	public static void writeFileFromArray(List<String> lines, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Allows writing a file with a StringBuilder.
	 *
	 * @param builder the builder
	 */
	public static void writeFileFromBuilder(StringBuilder builder, String filePath) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.write(builder.toString());
			System.out.println("The file " + filePath + " was created successfully!");
		} catch (IOException e) {
			System.out.println("Error creating file " + filePath + ": " + e.getMessage());
		}
	}
	
	/**
	 * Adding one builder to another.
	 *
	 * @param sourceBuilder the builder to add
	 * @param targetBuilder the builder who receives
	 * @param beginOfLine   each line begins with this string
	 * @param endOfLine     each line ends with this string
	 */
	public static void appendStringBuilders(StringBuilder sourceBuilder, StringBuilder targetBuilder, String beginOfLine, String endOfLine) {
		String[] lines = sourceBuilder.toString().split("\n");
		for (String line : lines) {
			targetBuilder.append(beginOfLine).append(line).append(endOfLine);
		}
	}
	
	/**
	 * Allows to read lines iteratively from a builder.
	 *
	 * @param builder the builder in question
	 * @param line    the lambda expression to run
	 */
	public static void readBuilderLines(StringBuilder builder, Consumer<String> line) {
		String[] lines = builder.toString().split("\n");
		for (String builderLine : lines) {

			line.accept(builderLine);
		}
	}
	
	/**
	 * Allows you to read the lines of a file and make changes to them.
	 *
	 * @param filepath file path
	 * @param line     lambda expression
	 */
	public static void readFileLines(String filepath, Consumer<String> line) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			String stringLine;
			while ((stringLine = reader.readLine()) != null) {
				line.accept(stringLine);
			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}
	}
	
	/**
	 * Extract lines matching a certain pattern from the file.
	 *
	 * @param filePath      file path
	 * @param regex_pattern the recovery pattern
	 * @return list of rows extracted
	 */
	public static ArrayList<String> extractPatternFromFile(String filePath, String regex_pattern) {
		ArrayList<String> lines = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			Pattern pattern = Pattern.compile(regex_pattern);
			
			String line;
			while ((line = reader.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					lines.add(matcher.group());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	/**
	 * Allows you to extract the name of a function.
	 *
	 * @param signature function signature
	 * @return the name of the function
	 */
	public static String extractFunctionName(String signature) {
		// Set regex pattern to extract function name
		String regex = "\\b([A-Za-z_][A-Za-z0-9_]*)\\s*\\(";
		
		// Create the pattern and the matcher for the signature
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(signature);
		
		// Check if the function name was found
		if (matcher.find()) {
			return matcher.group(1); // Return the first captured group
		}
		
		// If no function name is found, return an empty string or handle the error as needed
		return "";
	}
	
	/**
	 * Allows to extract the types and names of the parameters of a function in order of appearance.
	 *
	 * @param signature function signature
	 * @return the list of parameter types and names in order of appearance
	 */
	public static ArrayList<Pair<String, String>> extractParamTypesAndNames(String signature) {
		ArrayList<Pair<String, String>> paramTypesAndNames = new ArrayList<>();
		
		// Using a regular expression to extract parameter types and names
		Pattern pattern = Pattern.compile("(\\w+(?:\\[])?)\\s+(\\w+)\\b(?=\\s*,|\\s*\\))");
		Matcher matcher = pattern.matcher(signature);
		
		while (matcher.find()) {
			String paramType = matcher.group(1);
			String paramName = matcher.group(2);
			paramTypesAndNames.add(new Pair<>(paramType, paramName));
		}
		
		return paramTypesAndNames;
	}
	
	/**
	 * Allows to extract the names of the parameters of a function.
	 *
	 * @param signature function signature
	 * @return the list of parameter names
	 */
	public static List<String> extractParamNames(String signature) {
		List<String> paramNames = new ArrayList<>();
		
		// Using a regular expression to extract parameter names
		Pattern pattern = Pattern.compile("\\b\\w+\\b(?=\\s*,|\\s*\\))");
		Matcher matcher = pattern.matcher(signature);
		
		while (matcher.find()) {
			String paramName = matcher.group();
			paramNames.add(paramName);
		}
		
		return paramNames;
	}
	
	/**
	 * Gives the types of the function's parameters and return from a line corresponding to the format of a function.
	 *
	 * @param signature function signature
	 * @return types list
	 */
	public static ArrayList<String> extractFunctionTypes(String signature) {
		Pattern pattern = Pattern.compile("(\\w+(?:\\[])?)\\s+\\w+\\s*\\(([^)]*)\\)");
		Matcher matcher = pattern.matcher(signature);
		
		ArrayList<String> typesList = new ArrayList<>();
		while (matcher.find()) {
			String returnType = matcher.group(1);
			String paramTypes = matcher.group(2);
			
			ArrayList<String> paramTypeArray = new ArrayList<>(List.of(paramTypes.split("\\s\\w+,\\s|\\s\\w+")));
			
			if (!returnType.isBlank()) typesList.add(returnType); // added function return type
			if (!paramTypeArray.isEmpty()) if (!paramTypeArray.get(0).isEmpty()) typesList.addAll(paramTypeArray);
		}
		
		return typesList;
	}
	
	/**
	 * Allows you to retrieve the values of a list without the [ ].
	 *
	 * @param list the list containing character strings
	 * @return a string of list values
	 */
	public static String getListWithoutBrackets(List<String> list) {
		String stringRepresentation = list.toString(); // Convert list to String
		return stringRepresentation.replace("[", "").replace("]", ""); // Remove '[' and ']'
	}
	
	/**
	 * Delete it ; at the end of a line.
	 *
	 * @param input the line string
	 * @return the line without the semicolon
	 */
	public static String removeSemicolon(String input) {
		if (input.endsWith(";")) {
			return input.substring(0, input.length() - 1);
		}
		return input;
	}
	
	/**
	 * Extract all the keys from a {@link Pair} type.
	 *
	 * @param pairList list of pair
	 * @param <K>      key
	 * @param <V>      value
	 * @return list of keys of all the pairs
	 */
	public static <K, V> List<K> extractPairKeys(List<Pair<K, V>> pairList) {
		var keysList = new ArrayList<K>();
		for (var pair : pairList) {
			keysList.add(pair.key());
		}
		return keysList;
	}
	
	/**
	 * Extract all the values from a {@link Pair} type.
	 *
	 * @param pairList list of pair
	 * @param <K>      key
	 * @param <V>      value
	 * @return list of values of all the pairs
	 */
	public static <K, V> List<V> extractPairValues(List<Pair<K, V>> pairList) {
		var valuesList = new ArrayList<V>();
		for (var pair : pairList) {
			valuesList.add(pair.value());
		}
		return valuesList;
	}
	
	/**
	 * Retrieves the key corresponding to the specified value from the given map.
	 *
	 * @param map   the map containing key-value pairs
	 * @param value the value whose key is to be retrieved
	 * @param <K>   the type of keys in the map
	 * @param <V>   the type of values in the map
	 * @return the key associated with the specified value, or {@code null} if not found
	 */
	public static <K, V> K getKeyFromValue(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null; // value not found
	}
	
	/**
	 * Modifies a list by replacing a target element with a new value.
	 *
	 * @param list   the original list.
	 * @param target the target element to be replaced.
	 * @param value  the new value to replace the target element.
	 * @return a new unmodifiable list with the target element replaced.
	 */
	public static List<String> modifyList(List<String> list, String target, String value) {
		List<String> modifiedList = new ArrayList<>(list);
		int index = modifiedList.indexOf(target);
		
		if (index != -1) {
			modifiedList.set(index, value);
		}
		
		return Collections.unmodifiableList(modifiedList);
	}
	
	/**
	 * Format all the lines in the list and return the string formatted.
	 *
	 * @param lines       list of lines
	 * @param beginOfLine characters of begin of each line
	 * @param endOfLine   characters of end of each line
	 * @return the lines concatenated and formatted
	 */
	public static String formattingLineList(List<String> lines, String beginOfLine, String endOfLine) {
		StringBuilder formattedString = new StringBuilder();
		
		for (String line : lines) {
			/* Add beginOfLine at the beginning and endOfLine at the end of each line */
			formattedString.append(beginOfLine).append(line).append(endOfLine);
		}
		
		return formattedString.toString();
	}
	
	/**
	 * Format the line and return the string formatted.
	 *
	 * @param line        line
	 * @param beginOfLine characters of begin of the line
	 * @param endOfLine   characters of end of the line
	 * @return the line formatted
	 */
	public static String formattingLine(String line, String beginOfLine, String endOfLine) {
		return beginOfLine + line + endOfLine;
	}
	
	/**
	 * Replaces occurrences of types from the provided dictionary in the given line.
	 *
	 * @param typesReplacement a dictionary where keys are old types and values are new types.
	 * @param line             the input line of text containing type declarations.
	 * @return the line with replaced type occurrences.
	 */
	public static String replaceTypes(Map<String, String> typesReplacement, String line) {
		for (Map.Entry<String, String> entry : typesReplacement.entrySet()) {
			String oldType = entry.getKey();
			String newType = entry.getValue();
			line = line.replaceAll("(^|\\(|\\s)" + oldType + "(\\s|\\[\\])", "$1" + newType + "$2");
		}
		return line;
	}
}