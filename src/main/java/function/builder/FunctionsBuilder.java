package function.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
	private static final String FILE_PATH = "src/main/java/function/builder/";
	private static final String TMP_PATH = FILE_PATH + "tmp/"; // Dossier des fichiers temporaires
	private static final String C_FUNCTIONS_PATH = TMP_PATH + "functions.h"; // Fichier généré par la classe FunctionsExtractor
	private static final String FUNCTIONS_PATH = TMP_PATH + "generatedFunctions.tmp"; // Fichier généré par cette classe
	private static final String INTERFACE_PATH = TMP_PATH + "generatedInterface.java"; // Fichier généré par cette classe
	private static final String CLASS_PATH = FILE_PATH + "generatedClass.java"; // Fichier généré par cette classe
	public static ArrayList<String> unSupportedTypes = new ArrayList<>(); // Liste des types non-supportés
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
		typeChange.put("\\*char", "String");
		typeChange.put("\\*\\[\\]", "Pointer[]");
		typeChange.put("\\*", "Pointer");
		typeChange.put("void", "void");
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
		/**
		 * Génère le fichier des fonctions
		 */
		readFileLines(C_FUNCTIONS_PATH, line -> {
			String processedLine = changeFunctionType(line) + "\n";
			functionsBuilder.append(processedLine);
		});
		writeFile(FUNCTIONS_PATH, functionsBuilder);
		System.out.println("Types non-supportés: " + unSupportedTypes);
		
		/**
		 * Génère le fichier de l'interface
		 */
		interfaceBuilder.append("""
				public interface MeosLibrary {
					functions.MeosLibrary INSTANCE = LibraryLoader.create(functions.MeosLibrary.class).load("meos");
					functions.MeosLibrary meos = functions.MeosLibrary.INSTANCE;
					
				""");
		readFileLines(FUNCTIONS_PATH, line -> {
			String processedLine = "\t" + line + "\n";
			interfaceBuilder.append(processedLine);
		});
		interfaceBuilder.append("}");
		writeFile(INTERFACE_PATH, interfaceBuilder);
		
		/**
		 * Génère le fichier de la classe
		 */
		classBuilder.append("""
				package function.builder;
				
				import jnr.ffi.LibraryLoader;
				import jnr.ffi.Pointer;
				
				public class functions {
				""");
		readFileLines(INTERFACE_PATH, line -> {
			String processedLine = "\t" + line + "\n";
			classBuilder.append(processedLine);
		});
		readFileLines(FUNCTIONS_PATH, line -> {
			String functionSignature = "\t" + "public static " + removeSemicolon(line) + " {\n";
			classBuilder.append(functionSignature);
			String functionBody = "\t" + "    " + "return MeosLibrary.meos." + extractFunctionName(line) + "(" + getListWithoutBrackets(extractParamNames(line)) + ");\n\t}\n";
			classBuilder.append(functionBody);
		});
		classBuilder.append("}");
		writeFile(CLASS_PATH, classBuilder);
	}
	
	public static String removeSemicolon(String input) {
		if (input.endsWith(";")) {
			return input.substring(0, input.length() - 1);
		}
		return input;
	}
	
	public static String getListWithoutBrackets(ArrayList<String> list) {
		// Convertir l'ArrayList en une chaîne de caractères
		String stringRepresentation = list.toString();
		
		// Supprimer les '[' et ']'
		return stringRepresentation.replace("[", "").replace("]", "");
	}
	
	public static String extractFunctionName(String signature) {
		// Définir le motif regex pour extraire le nom de la fonction
		String regex = "\\b([A-Za-z_][A-Za-z0-9_]*)\\s*\\(";
		
		// Créer le pattern et le matcher pour la signature
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(signature);
		
		// Vérifier si le nom de la fonction a été trouvé
		if (matcher.find()) {
			return matcher.group(1); // Retourner le premier groupe capturé
		}
		
		// Si aucun nom de fonction n'est trouvé, retourner une chaîne vide ou gérer l'erreur selon vos besoins
		return "";
	}
	
	public static ArrayList<String> extractParamNames(String signature) {
		ArrayList<String> paramNames = new ArrayList<>();
		
		// Utilisation d'une expression régulière pour extraire les noms des paramètres
		Pattern pattern = Pattern.compile("\\b\\w+\\b(?=\\s*,|\\s*\\))");
		Matcher matcher = pattern.matcher(signature);
		
		while (matcher.find()) {
			String paramName = matcher.group();
			paramNames.add(paramName);
		}
		
		return paramNames;
	}
	
	private static void generateClass() {
	
	}
	
	/**
	 * Permet de lire les lignes d'un fichier et d'effectuer des modifications sur celles-ci.
	 *
	 * @param filepath chemin du fichier
	 * @param process  expression lambda
	 */
	private static void readFileLines(String filepath, Consumer<String> process) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				process.accept(line);
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
	private static String changeFunctionType(String line) {
		// Supprimer les mots clés "extern" et "const"
		line = line.replace("extern ", "").replace("const ", "");
		//        System.out.println(line);
		
		// Modification des types avec *
		line = line.replaceAll("char\\s\\*", "*char ");
		line = line.replaceAll("\\w+\\s\\*\\*", "*[] ");
		line = line.replaceAll("\\w+\\s\\*(?!\\*)", "* ");
		//		System.out.println(line);
		
		// Modification des types spéciaux
		line = line.replaceAll("\\*char\\stz_str", "byte[] tz_str"); // Pour la fonction meos_initialize(const char *tz_str);
		line = line.replaceAll("\\(void\\)", "()"); // Pour la fonction meos_finish(void);
		
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
	 *
	 * @param filepath le chemin de création du fichier
	 * @param builder  le builder
	 */
	private static void writeFile(String filepath, StringBuilder builder) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
			writer.write(builder.toString());
			System.out.println("Le fichier " + filepath + " a été créé avec succès !");
		} catch (IOException e) {
			System.out.println("Erreur lors de la création du fichier " + filepath + ": " + e.getMessage());
		}
	}
}