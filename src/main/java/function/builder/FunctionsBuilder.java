package function.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe permettant de construire le fichier de fonctions.
 *
 * @author Killian Monnier
 * @since 27/06/2023
 */
public class FunctionsBuilder {
	public static final HashMap<String, String> TYPES = typesBuild(); // Création du dictionnaire des types C et de son équivalent en Java
	public static ArrayList<String> unSupportedTypes = new ArrayList<>(); // Liste des types non-supportés
	private static final String FILE_PATH = "src/main/java/function/builder/";
	private static final String TMP_PATH = FILE_PATH + "function.tmp"; // Fichier généré par la classe FunctionsExtractor
	private static final String FUNCTIONS_PATH = FILE_PATH + "functions_generated.java"; // Fichier généré par cette classe
	public static StringBuilder functionsBuilder = new StringBuilder(); // Builder pour le fichier temporaire de fonctions
	public static StringBuilder interfaceBuilder = new StringBuilder(); // Builder pour la partie interface de la classe
	public static StringBuilder classBuilder = new StringBuilder(); // Builder de la classe
	
	/**
	 * Construit le tableau de modification des types.
	 * <p>
	 * Clé: ancien type
	 * <p>
	 * Valeur: nouveau type
	 *
	 * @return dictionnaire des types
	 */
	private static HashMap<String, String> typesBuild() {
		HashMap<String, String> typeChange = new HashMap<>();
		typeChange.put("\\*\\[\\]", "Pointer[]");
		typeChange.put("\\*", "Pointer");
		typeChange.put("void", "void");
		typeChange.put("String", "String"); // char * : déjà modifié par la regex
		typeChange.put("bool", "boolean");
		typeChange.put("float", "float");
		typeChange.put("double", "double");
		typeChange.put("int32", "int");
		typeChange.put("uint8_t", "int");
		typeChange.put("Timestamp", "int");
		typeChange.put("TimestampTz", "int");
		typeChange.put("size_t", "int");
		typeChange.put("Datum", "int");
		typeChange.put("int", "int");
		
		return typeChange;
	}
	
	/**
	 * Fonction principale d'exécution du script.
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
//		generateHeader();
		generateFunctions();
		writeFile(FUNCTIONS_PATH, functionsBuilder);
	}
	
	/**
	 * Génère le header du fichier des fonctions.
	 */
	private static void generateHeader() {
		classBuilder.append("""
				package function;
				
				import jnr.ffi.LibraryLoader;
				import jnr.ffi.Pointer;
				
				public class functions {
				    public interface MeosLibrary {
				        functions.MeosLibrary INSTANCE = LibraryLoader.create(functions.MeosLibrary.class).load("meos");
				        functions.MeosLibrary meos = functions.MeosLibrary.INSTANCE;""");
	}
	
	/**
	 * Traite la génération des fonctions.
	 */
	private static void generateFunctions() {
		try (BufferedReader reader = new BufferedReader(new FileReader(TMP_PATH))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String processedLine = processLine(line);
				//				System.out.println(processedLine);
			}
		} catch (IOException e) {
			System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
		}
	}
	
	/**
	 * Traite les lignes permettant de générer les fonctions.
	 *
	 * @param line la ligne correspondant à une fonction
	 * @return la ligne traitée
	 */
	private static String processLine(String line) {
		// Supprimer les mots clés "extern" et "const"
		line = line.replace("extern ", "").replace("const ", "");
		//        System.out.println(line);
		
		// Modification des pointeurs
		line = line.replaceAll("char\\s\\*", "String ");
		line = line.replaceAll("\\w+\\s\\*\\*", "*[] ");
		line = line.replaceAll("\\w+\\s\\*(?!\\*)", "* ");
		//		System.out.println(line);
		
		// Remplace les types depuis le dictionnaire de types
		for (Map.Entry<String, String> entry : TYPES.entrySet()) {
			String oldType = entry.getKey();
			String newType = entry.getValue();
			line = line.replaceAll(oldType + "\\s", newType + " ");
		}
		//		System.out.println(line);
		
		List<String> typesNotSupported = getTypes(line).stream().filter(type -> !TYPES.containsValue(type)).toList(); // Récupération des types non-supportés pour la ligne
		unSupportedTypes.addAll(typesNotSupported.stream().filter(type -> !unSupportedTypes.contains(type)).toList()); // Récupération des types non-supportés qui ne sont pas encore dans la liste globale
		
		return line;
	}
	
	/**
	 * Donne les types des paramètres et de retour de la fonction depuis une ligne correspondant au format d'une fonction.
	 *
	 * @param line ligne texte d'une fonction
	 * @return liste des types
	 */
	private static ArrayList<String> getTypes(String line) {
		Pattern pattern = Pattern.compile("\\b(\\w+)\\b\\s+(\\w+)\\s*\\(([^)]*)\\)");
		Matcher matcher = pattern.matcher(line);
		
		ArrayList<String> typesList = new ArrayList<>();
		while (matcher.find()) {
			String returnType = matcher.group(1);
			String functionName = matcher.group(2);
			String paramTypes = matcher.group(3);
			
			String[] paramTypeArray = paramTypes.split("\\s\\w+,\\s|\\s\\w+");
			
			typesList.addAll(List.of(paramTypeArray));
			typesList.add(returnType); // ajout du type de retour de la fonction
		}
		
		return typesList;
	}
	
	/**
	 * Permet d'écrire un fichier avec un StringBuilder.
	 * @param path le chemin de création du fichier
	 * @param builder le builder
	 */
	private static void writeFile(String path, StringBuilder builder) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
			writer.write(builder.toString());
			System.out.println("Le fichier " + path + " a été créé avec succès !");
		} catch (IOException e) {
			System.out.println("Erreur lors de la création du fichier " + path + ": " + e.getMessage());
		}
	}
}