package function.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe permettant d'extraire les fonctions de la librairie MEOS.
 * À exéctuer comme suit :
 * <ul>
 *     <li>cd src\main\java\function\builder</li>
 *     <li>javac .\FunctionsExtractor.java</li>
 *     <li>java .\FunctionsExtractor.java</li>
 * </ul>
 *
 * @author Killian Monnier
 * @since 27/06/2023
 */
public class FunctionsExtractor {
	private static final String FILE_PATH = "";
	private static final String INPUT_FILE_PATH = FILE_PATH + "meos.h";
	private static final String OUTPUT_FUNCTIONS_PATH = FILE_PATH + "tmp/functions.h";
	private static final String OUTPUT_TYPES_PATH = FILE_PATH + "tmp/types.h";
	
	/**
	 * Modèle RegEx de reconnaissance d'une fonction dans le fichier meos.h
	 */
	private static final String FUNCTION_PATTERN = "extern (static inline )?[a-zA-Z0-9_]+ [a-zA-Z0-9_*]+\\([a-zA-Z0-9_* ,]+\\);";
	
	/**
	 * Modèle RegEx de reconnaissance d'un type dans le fichier meos.h
	 */
	private static final String TYPES_PATTERN = "typedef\\s(?!struct|enum)\\w+\\s\\w+;";
	
	/**
	 * Fonction de lancement du script.
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		ArrayList<String> functions = extractPatternFromFile(INPUT_FILE_PATH, FUNCTION_PATTERN);
		writeLinesToFile(functions, OUTPUT_FUNCTIONS_PATH);
		System.out.println("Extraction des fonctions terminée. Les fonctions ont été écrites dans le fichier " + OUTPUT_FUNCTIONS_PATH + ".");
		ArrayList<String> types = getTypesFromFile(INPUT_FILE_PATH, TYPES_PATTERN);
		writeLinesToFile(types, OUTPUT_TYPES_PATH);
		System.out.println("Extraction des types terminée. Les types ont été écrits dans le fichier " + OUTPUT_TYPES_PATH + ".");
	}
	
	/**
	 * Récupère les types depuis un fichier.
	 *
	 * @param inputFilePath le chemin du fichier
	 * @param typesPattern  le pattern de la ligne du type
	 * @return la liste des lignes
	 */
	private static ArrayList<String> getTypesFromFile(String inputFilePath, String typesPattern) {
		ArrayList<String> rawTypes = extractPatternFromFile(inputFilePath, typesPattern);
		ArrayList<String> structureNames = getStructureNames(inputFilePath);
		ArrayList<String> filteredTypes = new ArrayList<>();
		
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
	 * Récupère les noms des structures dans le fichier.
	 *
	 * @param filePath chemin du fichier
	 * @return la liste des noms des structures
	 */
	public static ArrayList<String> getStructureNames(String filePath) {
		List<String> structureNames = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
			
			String regex = "typedef\\s+struct(\\s\\w+)?\\s*\\{[\\s\\S]*?\\}\\s*(\\w+)";
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
	
	/**
	 * Extrait les lignes correspondent à un certain pattern depuis le fichier.
	 *
	 * @param filePath      chemin du fichier
	 * @param regex_pattern le pattern de récupération
	 * @return liste des lignes extraites
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
	 * Écrit les lignes dans un fichier.
	 *
	 * @param lines    la liste des lignes extraites
	 * @param filePath le chemin du fichier
	 */
	public static void writeLinesToFile(ArrayList<String> lines, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
