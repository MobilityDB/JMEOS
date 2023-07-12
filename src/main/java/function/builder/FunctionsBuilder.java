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
 * À exéctuer comme suit :
 * <ul>
 *     <li>cd src\main\java\function\builder</li>
 *     <li>javac .\FunctionsBuilder.java</li>
 *     <li>java .\FunctionsBuilder.java</li>
 * </ul>
 *
 * @author Killian Monnier
 * @since 27/06/2023
 */
public class FunctionsBuilder {
	private static final String FILE_PATH = "";
	private static final String C_FUNCTIONS_PATH = FILE_PATH + "tmp/functions.h"; // Fichier généré par la classe FunctionsExtractor
	private static final String C_TYPES_PATH = FILE_PATH + "tmp/types.h"; // Fichier généré par la classe FunctionsExtractor
	private static final HashMap<String, String> TYPES = typesBuild(); // Création du dictionnaire des types C et de son équivalent en Java
	private static final String FUNCTIONS_CLASS_PATH = FILE_PATH + "../functions.java"; // Classe functions générée
	private static final ArrayList<String> unSupportedTypes = new ArrayList<>(); // Liste des types non-supportés
	
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
		typeChange.put("\\*", "Pointer");
		typeChange.put("\\*\\[\\]", "Pointer[]");
		typeChange.put("byte\\[\\]", "byte[]");
		typeChange.put("\\*char", "String");
		typeChange.put("void", "void");
		typeChange.put("bool", "boolean");
		typeChange.put("float", "float");
		typeChange.put("double", "double");
		typeChange.put("int", "int");
		typeChange.put("int32", "int32_t");
		typeChange.put("int64", "int64_t");
		typeChange.put("uint8_t", "u_int8_t");
		typeChange.put("uint16_t", "u_int16_t");
		typeChange.put("uint32", "u_int32_t");
		typeChange.put("uint64", "u_int64_t");
		typeChange.put("uintptr_t", "uintptr_t");
		typeChange.put("size_t", "size_t");
		typeChange.put("interpType", "int"); // enum en C
		
		readFileLines(C_TYPES_PATH, line -> { // Ajout des typedef extraits du fichier C
			Pattern pattern = Pattern.compile("^typedef\\s(\\w+)\\s(\\w+);");
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String rawType = matcher.group(1);
				String typeDef = matcher.group(2);
				
				if (typeChange.containsKey(rawType)) rawType = typeChange.get(rawType);
				
				typeChange.put(typeDef, rawType);
			} else {
				System.out.println("Extraction du type impossible pour la ligne: " + line);
			}
		});
		
		System.out.println(typeChange);
		
		return typeChange;
	}
	
	/**
	 * Fonction principale d'exécution du script.
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		StringBuilder functionsBuilder = generateFunctions(C_FUNCTIONS_PATH);
		StringBuilder interfaceBuilder = generateInterface(functionsBuilder);
		StringBuilder classBuilder = generateClass(functionsBuilder, interfaceBuilder);
		writeFile(FUNCTIONS_CLASS_PATH, classBuilder);
	}
	
	/**
	 * Permet de générer la classe des fonctions.
	 *
	 * @param functionsBuilder builder des fonctions
	 * @param interfaceBuilder builder de l'interface
	 * @return le builder de la classe
	 */
	private static StringBuilder generateClass(StringBuilder functionsBuilder, StringBuilder interfaceBuilder) {
		StringBuilder builder = new StringBuilder();
		builder.append("""
				package function;
				
				import jnr.ffi.LibraryLoader;
				import jnr.ffi.Pointer;
				import jnr.ffi.types.int32_t;
				import jnr.ffi.types.int64_t;
				import jnr.ffi.types.u_int8_t;
				import jnr.ffi.types.u_int16_t;
				import jnr.ffi.types.u_int32_t;
				import jnr.ffi.types.u_int64_t;
				import jnr.ffi.types.uintptr_t;
				import jnr.ffi.types.size_t;
						
				public class functions {
				""");
		appendBuilderWith(interfaceBuilder, builder, "\t", "\n\n"); // Ajout de l'interface
		
		StringBuilder functionBodyBuilder = new StringBuilder(); // Ajout des fonctions
		readBuilderLines(functionsBuilder, line -> {
			if (!line.isBlank()) {
				String functionSignature = "public static " + removeSemicolon(line) + " {\n";
				functionBodyBuilder.append(functionSignature);
				String functionBody = "\t" + "return MeosLibrary.meos." + extractFunctionName(line) + "(" + getListWithoutBrackets(extractParamNames(line)) + ");\n}\n\n";
				
				if (getFunctionTypes(line).get(0).equals("void"))
					functionBody = "\t" + "MeosLibrary.meos." + extractFunctionName(line) + "(" + getListWithoutBrackets(extractParamNames(line)) + ");\n}\n\n"; // Quand la fonction ne renvoie rien
				
				functionBodyBuilder.append(functionBody);
			}
		});
		appendBuilderWith(functionBodyBuilder, builder, "\t", "\n");
		builder.append("}");
		return builder;
	}
	
	/**
	 * Génération de l'interface.
	 *
	 * @param functionsBuilder builder des fonctions
	 * @return le builder de l'interface
	 */
	private static StringBuilder generateInterface(StringBuilder functionsBuilder) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("""
				public interface MeosLibrary {
					functions.MeosLibrary INSTANCE = LibraryLoader.create(functions.MeosLibrary.class).load("meos");
					functions.MeosLibrary meos = functions.MeosLibrary.INSTANCE;
				""");
		appendBuilderWith(functionsBuilder, builder, "\t", "\n");
		builder.append("}");
		return builder;
	}
	
	/**
	 * Génération des fonctions avec leurs types modifiés.
	 *
	 * @param functions_path chemin des fonctions C
	 * @return le builder des fonctions
	 */
	private static StringBuilder generateFunctions(String functions_path) {
		StringBuilder builder = new StringBuilder();
		
		readFileLines(functions_path, line -> {
			String processedLine = changeFunctionType(line) + "\n";
			builder.append(processedLine);
		});
		System.out.println("Types non-supportés: " + unSupportedTypes);
		return builder;
	}
	
	/**
	 * Ajout d'un builder dans un autre.
	 *
	 * @param sourceBuilder le builder à ajouter
	 * @param targetBuilder le builder qui reçoit
	 * @param startOfLine   chaque ligne commence par cette chaîne de caractères
	 * @param endOfLine     chaque ligne finit par cette chaîne de caractères
	 */
	public static void appendBuilderWith(StringBuilder sourceBuilder, StringBuilder targetBuilder, String startOfLine, String endOfLine) {
		String[] lines = sourceBuilder.toString().split("\n");
		for (String line : lines) {
			targetBuilder.append(startOfLine).append(line).append(endOfLine);
		}
	}
	
	/**
	 * Permet de lire les lignes itérativement depuis un builder.
	 *
	 * @param builder le builder en question
	 * @param process l'expression lambda à lancer
	 */
	private static void readBuilderLines(StringBuilder builder, Consumer<String> process) {
		String[] lines = builder.toString().split("\n");
		for (String line : lines) {
			process.accept(line);
		}
	}
	
	/**
	 * Supprime le ; à la fin d'une ligne.
	 *
	 * @param input la chaîne de caractères de la ligne
	 * @return la ligne sans le ;
	 */
	public static String removeSemicolon(String input) {
		if (input.endsWith(";")) {
			return input.substring(0, input.length() - 1);
		}
		return input;
	}
	
	/**
	 * Permet de récupérer les valeurs d'une liste sans les [ ].
	 *
	 * @param list la liste comportant des chaînes de caractères
	 * @return une chaîne de caractères des valeurs de la liste
	 */
	public static String getListWithoutBrackets(ArrayList<String> list) {
		String stringRepresentation = list.toString(); // Convertir l'ArrayList en une chaîne de caractères
		return stringRepresentation.replace("[", "").replace("]", ""); // Supprimer les '[' et ']'
	}
	
	/**
	 * Permet d'extraire le nom d'une fonction.
	 *
	 * @param signature signature de la fonction
	 * @return le nom de la fonction
	 */
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
	
	/**
	 * Permet d'extraire les noms des paramètres d'une fonction.
	 *
	 * @param signature signature de la fonction
	 * @return la liste des noms de paramètres
	 */
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
	
	/**
	 * Traite les lignes permettant de générer les fonctions.
	 *
	 * @param line la ligne correspondant à une fonction
	 * @return la ligne traitée
	 */
	private static String changeFunctionType(String line) {
		if (!line.isBlank()) {
			// Supprimer les mots clés qui ne nous intéresse pas
			line = line.replaceAll("extern ", "");
			line = line.replaceAll("const ", "");
			line = line.replaceAll("static inline ", "");
			
			// Modification des types avec *
			line = line.replaceAll("char\\s\\*", "*char ");
			line = line.replaceAll("\\w+\\s\\*\\*", "*[] ");
			line = line.replaceAll("\\w+\\s\\*(?!\\*)", "* ");
			
			// Modification des types ou noms spéciaux
			line = line.replaceAll("\\*char\\stz_str", "byte[] tz_str"); // Pour la fonction meos_initialize(const char *tz_str);
			line = line.replaceAll("\\(void\\)", "()"); // Pour la fonction meos_finish(void);
			line = line.replaceAll("synchronized", "synchronize"); // Pour la fonction temporal_simplify(const Temporal *temp, double eps_dist, bool synchronized);
			
			// Remplace les types depuis le dictionnaire de types
			for (Map.Entry<String, String> entry : TYPES.entrySet()) {
				String oldType = entry.getKey();
				String newType = entry.getValue();
				line = line.replaceAll("((^|\\(|\\s)+)" + oldType + "\\s", "$1" + newType + " ");
			}
			
			List<String> typesNotSupported = getFunctionTypes(line).stream().filter(type -> !TYPES.containsValue(type)).toList(); // Récupération des types non-supportés pour la ligne
			unSupportedTypes.addAll(typesNotSupported.stream().filter(type -> !unSupportedTypes.contains(type)).toList()); // Récupération des types non-supportés qui ne sont pas encore dans la liste globale
		}
		
		return line;
	}
	
	/**
	 * Donne les types des paramètres et de retour de la fonction depuis une ligne correspondant au format d'une fonction.
	 *
	 * @param line ligne texte d'une fonction
	 * @return liste des types
	 */
	private static ArrayList<String> getFunctionTypes(String line) {
		Pattern pattern = Pattern.compile("(\\w+(?:\\[\\])?)\\s+(\\w+)\\s*\\(([^)]*)\\)");
		Matcher matcher = pattern.matcher(line);
		
		ArrayList<String> typesList = new ArrayList<>();
		while (matcher.find()) {
			String returnType = matcher.group(1);
			String functionName = matcher.group(2);
			String paramTypes = matcher.group(3);
			
			ArrayList<String> paramTypeArray = new ArrayList<>(List.of(paramTypes.split("\\s\\w+,\\s|\\s\\w+")));
			
			if (!returnType.isBlank()) typesList.add(returnType); // ajout du type de retour de la fonction
			if (!paramTypeArray.isEmpty())
				if (!paramTypeArray.get(0).isEmpty())
					typesList.addAll(paramTypeArray);
		}
		
		return typesList;
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