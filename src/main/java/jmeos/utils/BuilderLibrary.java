package jmeos.utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java library for manipulating StringBuilders as well as strings to allow string modification, reading and file writing operations.
 *
 * @author Killian Monnier
 * @since 27/07/2023
 */
public class BuilderLibrary {
	
	public static String getFilePath(Class<?> className) {
		// Obtenir l'URL de la classe en cours (MyClass dans cet exemple)
		URL url = className.getProtectionDomain().getCodeSource().getLocation();
		
		// Convertir l'URL en chemin de fichier
		File classFile = new File(url.getFile());
		
		// Récupérer le répertoire parent (répertoire du package)
		Path currRelativePath = Paths.get("");
		String currAbsolutePathString = currRelativePath.toAbsolutePath().toString();
		System.out.println("Current absolute path is - " + currAbsolutePathString);
		
		return classFile.getParent();
	}
	
	/**
	 * Writes lines to a file.
	 *
	 * @param lines    the list of extracted rows
	 * @param filePath the file path
	 */
	public static void writeFileFromArray(ArrayList<String> lines, String filePath) {
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
	 * @param startOfLine   each line starts with this string
	 * @param endOfLine     each line ends with this string
	 */
	public static void appendStringBuilders(StringBuilder sourceBuilder, StringBuilder targetBuilder, String startOfLine, String endOfLine) {
		String[] lines = sourceBuilder.toString().split("\n");
		for (String line : lines) {
			targetBuilder.append(startOfLine).append(line).append(endOfLine);
		}
	}
	
	/**
	 * Allows to read lines iteratively from a builder.
	 *
	 * @param builder the builder in question
	 * @param process the lambda expression to run
	 */
	public static void readBuilderLines(StringBuilder builder, Consumer<String> process) {
		String[] lines = builder.toString().split("\n");
		for (String line : lines) {
			process.accept(line);
		}
	}
	
	/**
	 * Allows you to read the lines of a file and make changes to them.
	 *
	 * @param filepath file path
	 * @param process  lambda expression
	 */
	public static void readFileLines(String filepath, Consumer<String> process) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				process.accept(line);
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
		List<String> lines = new ArrayList<>();
		
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
		
		return new ArrayList<>(lines);
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
	public static ArrayList<String> extractParamNames(String signature) {
		ArrayList<String> paramNames = new ArrayList<>();
		
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
	 * @param line text line of a function
	 * @return type list
	 */
	public static ArrayList<String> getFunctionTypes(String line) {
		Pattern pattern = Pattern.compile("(\\w+(?:\\[])?)\\s+\\w+\\s*\\(([^)]*)\\)");
		Matcher matcher = pattern.matcher(line);
		
		ArrayList<String> typesList = new ArrayList<>();
		while (matcher.find()) {
			String returnType = matcher.group(1);
			String paramTypes = matcher.group(2);
			
			ArrayList<String> paramTypeArray = new ArrayList<>(List.of(paramTypes.split("\\s\\w+,\\s|\\s\\w+")));
			
			if (!returnType.isBlank()) typesList.add(returnType); // added function return type
			if (!paramTypeArray.isEmpty()) if (!paramTypeArray.get(0).isEmpty()) typesList.addAll(paramTypeArray);
		}
		
		// Remove Array Types from the list then change it to normal type
		List<String> arrayTypesList = typesList.stream().filter(type -> type.contains("[]")).toList();
		typesList.removeAll(arrayTypesList);
		List<String> newTypesList = arrayTypesList.stream().map(arrayType -> arrayType.replace("[]", "")).toList();
		typesList.addAll(newTypesList);
		
		return typesList;
	}
	
	/**
	 * Allows you to retrieve the values of a list without the [ ].
	 *
	 * @param list the list containing character strings
	 * @return a string of list values
	 */
	public static String getListWithoutBrackets(ArrayList<String> list) {
		String stringRepresentation = list.toString(); // Convert ArrayList to String
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
}