package functions.builder;

import utils.builder.BuilderUtils;
import utils.builder.Pair;

import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to build the functions from the MEOS library.
 * Run with ./script folder
 *
 * @author Killian Monnier
 * @since 27/06/2a023
 */
public class FunctionsBuilder {
	private static final String BUILDER_PATH = "src/main/java/functions/builder/";
	private static final String C_FUNCTIONS_PATH = BUILDER_PATH + "tmp/meos_functions.h"; // File generated by the FunctionsExtractor class
	private static final String C_TYPES_PATH = BUILDER_PATH + "tmp/meos_types.h"; // File generated by the FunctionsExtractor class
	private static final String FUNCTIONS_CLASS_PATH = BUILDER_PATH + "../functions.java"; // Generated functions class
	private final HashMap<String, String> equivalentTypes = buildEquivalentTypes(); // Creation of the C type dictionary and its equivalent in Java
	private final ArrayList<String> unsupportedEquivalentTypes = new ArrayList<>(); // List of unsupported types
	private final ArrayList<String> unsupportedConversionTypedefs = new ArrayList<>(); // List of unsupported types
	private final HashMap<String, String> conversionTypes = buildConversionTypes(); // List of types to perform a conversion
	private final HashMap<String, String> conversionTypedefs = buildConversionTypedefs(); // List of types to perform a conversion
	private final ArrayList<String> unsupportedConversionTypes = new ArrayList<>(); // List of unsupported types
	
	/**
	 * Gives the types of the function's parameters and return from a line corresponding to the format of a function.
	 *
	 * @param signature function signature
	 * @return types list
	 */
	private static ArrayList<String> getFunctionTypes(String signature) {
		ArrayList<String> functionTypes = BuilderUtils.extractFunctionTypes(signature);
		
		/* Remove Array Types from the functionTypes list then change it to normal type */
		List<String> arrayTypesList = functionTypes.stream().filter(type -> type.contains("[]")).toList();
		functionTypes.removeAll(arrayTypesList);
		
		List<String> newTypesList = arrayTypesList.stream().map(arrayType -> arrayType.replace("[]", "")).toList();
		functionTypes.addAll(newTypesList);
		
		return functionTypes;
	}
	
	/**
	 * Main script execution function.
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		/* Retrieve the value of the JMEOS_PATH environment variable */
		String jmeosHome = System.getenv("JMEOS_HOME");
		
		/* Check if environment variable exists */
		if (jmeosHome != null) {
			System.out.println("JMEOS_HOME: " + jmeosHome);
			System.setProperty("user.dir", jmeosHome); // Set the current working directory
			
			var builder = new FunctionsBuilder();
			
			/* Generation of all the functions signature */
			StringBuilder functionsInterfaceBuilder = builder.generateFunctions(false);
			StringBuilder functionsClassBuilder = builder.generateFunctions(true);
			System.out.println("Unsupported types: " + builder.unsupportedEquivalentTypes);
			System.out.println("Unsupported conversion typedefs: " + builder.unsupportedConversionTypedefs);
			System.out.println("Unsupported conversion types: " + builder.unsupportedConversionTypes);
			
			/* Generation of the file */
			StringBuilder interfaceBuilder = builder.generateInterface(functionsInterfaceBuilder);
			StringBuilder classBuilder = builder.generateClass(functionsClassBuilder, interfaceBuilder);
			BuilderUtils.writeFileFromBuilder(classBuilder, FUNCTIONS_CLASS_PATH);
		} else {
			throw new InvalidPathException("JMEOS_HOME", "The JMEOS_HOME environment variable is not set.\nCancellation of build.");
		}
	}
	
	/**
	 * Processes the rows to generate the functions.
	 *
	 * @param line the line corresponding to a function
	 * @return the processed line
	 */
	private static String performTypeConversion(String line) {
		if (!line.isBlank()) {
			/* Remove keywords that are not of interest to us */
			line = line.replaceAll("extern ", "");
			line = line.replaceAll("const ", "");
			line = line.replaceAll("static inline ", "");
			
			/* Changing types with * */
			line = line.replaceAll("char\\s\\*", "*char ");
			line = line.replaceAll("\\w+\\s\\*\\*", "*[] ");
			line = line.replaceAll("\\w+\\s\\*(?!\\*)", "* ");
			
			/* Changing special types or names */
			line = line.replaceAll("\\(void\\)", "()"); // Remove the void parameter (for the function meos_finish(void)) //FIXME utiliser les Optional pour les param optionel look pymeos
			line = line.replaceAll("synchronized", "synchronize"); // Change the keyword used by Java (for the function temporal_simplify(const Temporal *temp, double eps_dist, bool synchronized))
		}
		
		return line;
	}
	
	/**
	 * Builds the type modification array.
	 * <pre>
	 * Key: old type in C or modified
	 * Value: new type in Java
	 * </pre>
	 *
	 * @return type dictionary
	 */
	private static HashMap<String, String> buildEquivalentTypes() {
		HashMap<String, String> types = new HashMap<>();
		types.put("\\*", "Pointer");
		types.put("\\*char", "String");
		types.put("Pointer\\[\\]", "Pointer"); // Keep this line, otherwise operand error in JNR-FFI
		types.put("bool", "boolean");
		types.put("float", "float");
		types.put("double", "double");
		types.put("void", "void");
		types.put("int", "int");
		types.put("short", "short");
		types.put("long", "long");
		types.put("int8", "byte");
		types.put("int16", "short");
		types.put("int32", "int");
		types.put("int64", "long");
		types.put("int8_t", "byte");
		types.put("int16_t", "short");
		types.put("int32_t", "int");
		types.put("int64_t", "long");
		types.put("uint8", "byte");
		types.put("uint16", "short");
		types.put("uint32", "int");
		types.put("uint64", "long");
		types.put("uint8_t", "byte");
		types.put("uint16_t", "short");
		types.put("uint32_t", "int");
		types.put("uint64_t", "long");
		types.put("uintptr_t", "long");
		types.put("size_t", "long");
		types.put("interpType", "int"); // enum in C
		
		return types;
	}
	
	/**
	 * Build the dictionary of typedef conversion.
	 * <pre>
	 * Key: old type in C or modified
	 * Value: new type in Java
	 * </pre>
	 *
	 * @return types dictionary
	 */
	private static HashMap<String, String> buildConversionTypedefs() {
		HashMap<String, String> typedefs = new HashMap<>();
		
		/* Added typedefs extracted from C file */
		BuilderUtils.readFileLines(C_TYPES_PATH, line -> {
			Pattern pattern = Pattern.compile("^typedef\\s(\\w+)\\s(\\w+);");
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String rawType = matcher.group(1);
				String typeDef = matcher.group(2);
				
				typedefs.put(typeDef, rawType);
			} else {
				System.err.println("Cannot extract type for row: " + line);
			}
		});
		return typedefs;
	}
	
	/**
	 * Build the dictionary of conversion types.
	 * <pre>
	 * Key: old type in C or modified
	 * Value: new type in Java
	 * </pre>
	 *
	 * @return types dictionary
	 */
	private static HashMap<String, String> buildConversionTypes() {
		HashMap<String, String> conversionTypes = new HashMap<>();
		conversionTypes.put("Timestamp", "LocalDateTime");
		conversionTypes.put("TimestampTz", "OffsetDateTime");
		return conversionTypes;
	}
	
	/**
	 * Check unsupported types.
	 *
	 * @param signature function signature
	 */
	private static List<String> getUnsupportedTypes(String signature, Map<String, String> types) {
		/* Retrieving unsupported types for the line */
		return getFunctionTypes(signature).stream().filter(type -> !types.containsValue(type)).toList();
	}
	
	/**
	 * Produce the process of adding code to handle conversion of certain types for a function.
	 *
	 * @param signature function signature
	 * @return list of {@link Pair} defined as follows :
	 * <ul>
	 *     <li>Key : {@link Pair} defined as follows :</li>
	 *     <ul>
	 *         <li>Key : old type name</li>
	 *         <li>Value : new type name</li>
	 *     </ul>
	 *     <li>Value : list of lines for the conversion process</li>
	 * </ul>
	 */
	private List<Pair<Pair<String, String>, List<String>>> generateConversionProcess(String signature) {
		List<Pair<Pair<String, String>, List<String>>> conversionList = new ArrayList<>();
		List<Pair<String, String>> typesList = BuilderUtils.extractParamTypesAndNames(signature);
		
		/* For each types */
		for (Pair<String, String> type : typesList) {
			String typeValue = type.key();
			
			/* If this type needs a conversion */
			if (conversionTypes.containsValue(typeValue)) {
				List<String> conversionLines = new ArrayList<>();
				String typeName = type.value();
				String newTypeName = typeName + "_new";
				
				switch (typeValue) {
					case "LocalDateTime" -> conversionLines.add("var " + newTypeName + " = " + typeName + ".toEpochSecond(ZoneOffset.UTC);");
					case "OffsetDateTime" -> conversionLines.add("var " + newTypeName + " = " + typeName + ".toEpochSecond();");
					default -> throw new TypeNotPresentException(typeValue, new Throwable("Type not supported by the builder conversion process"));
				}
				conversionList.add(new Pair<>(new Pair<>(typeName, newTypeName), conversionLines));
			}
		}
		return conversionList;
	}
	
	/**
	 * Produce the returning process for a function
	 *
	 * @param signature      function signature
	 * @param typesNamesList list of types names to change it in the calling of the equivalent interface function
	 * @return the returning process
	 */
	private List<String> generateReturnProcess(String signature, List<Pair<String, String>> typesNamesList) {
		List<String> functionCallingProcess = new ArrayList<>();
		List<String> paramNames = BuilderUtils.extractParamNames(signature);
		
		/* Manage the calling of meos library associate function */
		if (typesNamesList.isEmpty()) {
			functionCallingProcess.add("MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
		} else {
			/* Modify the names of the parameter types that has endured conversion */
			for (var typeNames : typesNamesList) {
				String oldName = typeNames.key();
				String newName = typeNames.value();
				paramNames = BuilderUtils.modifyList(paramNames, oldName, newName);
			}
			functionCallingProcess.add("MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
		} // TODO modifier ça
		
		/* Manage the return process : if there is something to return */
		if (!getFunctionTypes(signature).get(0).equals("void")) {
			var classReturnType = BuilderUtils.extractFunctionTypes(signature).get(0);
			
			/* Manage the returning process of conversion types */
			if (conversionTypes.containsValue(classReturnType)) {
				List<String> returnProcess = new ArrayList<>();
				var functionCall = BuilderUtils.removeSemicolon(functionCallingProcess.get(0));
				
				returnProcess.add("var result = " + functionCall + ";");
				
				switch (classReturnType) {
					case "LocalDateTime" -> returnProcess.add(
							"LocalDateTime.ofEpochSecond(result, 0, ZoneOffset.UTC);"
					);
					case "OffsetDateTime" -> returnProcess.addAll(List.of(
							"Instant instant = Instant.ofEpochSecond(result);",
							"OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);"
					));
					default -> throw new TypeNotPresentException(classReturnType, new Throwable("Type not supported by the builder returning conversion process"));
				}
				returnProcess.set(returnProcess.size() - 1, "return " + returnProcess.get(returnProcess.size() - 1)); // Add return at the last line of the list
				functionCallingProcess = returnProcess;
			} else {
				functionCallingProcess.set(0, "return " + functionCallingProcess.get(0));
			}
		}
		return functionCallingProcess;
	}
	
	/**
	 * Used to generate the class of functions.
	 *
	 * @param functionsBuilder builder of functions
	 * @param interfaceBuilder interface builder
	 * @return the class builder
	 */
	private StringBuilder generateClass(StringBuilder functionsBuilder, StringBuilder interfaceBuilder) {
		var builder = new StringBuilder();
		
		/* Get the package name */
		Package pkg = FunctionsBuilder.class.getPackage();
		String packageName = pkg.getName();
		String basePackage = packageName.substring(0, packageName.lastIndexOf('.'));
		String definePackage = "package " + basePackage + ";\n";
		
		String imports = """
				import jnr.ffi.Pointer;
				import utils.JarLibraryLoader;
				
				import java.time.*;
				""";
		String className = "public class functions {";
		builder.append(definePackage).append("\n").append(imports).append("\n").append(className).append("\n");
		
		/* Added interface */
		BuilderUtils.appendStringBuilders(interfaceBuilder, builder, "\t", "\n\n");
		
		/* Addition of functions */
		StringBuilder functionBodyBuilder = new StringBuilder();
		BuilderUtils.readBuilderLines(functionsBuilder, line -> {
			if (!line.isBlank()) {
				String functionSignature = "public static " + BuilderUtils.removeSemicolon(line) + " {\n";
				
				/* Generate the conversion process */
				var conversionProcess = this.generateConversionProcess(line);
				var conversionProcessList = BuilderUtils.extractPairValues(conversionProcess);
				var conversionProcessContent = conversionProcessList.stream().flatMap(Collection::stream).toList();
				
				/* Generate the returning process */
				var typesNamesList = BuilderUtils.extractPairKeys(conversionProcess);
				var returnProcessContent = this.generateReturnProcess(line, typesNamesList);
				
				/* Add all the different parts in the function body builder */
				functionBodyBuilder.append("@SuppressWarnings(\"unused\")\n")
						.append(functionSignature)
						.append(BuilderUtils.formattingLineList(conversionProcessContent, "\t", "\n"))
						.append(BuilderUtils.formattingLineList(returnProcessContent, "\t", "\n"))
						.append(BuilderUtils.formattingLine("}", "", "\n\n"));
			}
		});
		BuilderUtils.appendStringBuilders(functionBodyBuilder, builder, "\t", "\n");
		builder.append("}");
		return builder;
	}
	
	/**
	 * Generation of the interface.
	 *
	 * @param functionsBuilder builder of functions
	 * @return the interface builder
	 */
	private StringBuilder generateInterface(StringBuilder functionsBuilder) {
		var builder = new StringBuilder();
		
		builder.append("""
				public interface MeosLibrary {
					MeosLibrary INSTANCE = JarLibraryLoader.create(MeosLibrary.class, "meos").getLibraryInstance();
					MeosLibrary meos = MeosLibrary.INSTANCE;
				""");
		BuilderUtils.appendStringBuilders(functionsBuilder, builder, "\t", "\n");
		builder.append("}");
		return builder;
	}
	
	/**
	 * Generation of functions with their conversion types, typedef conversion types and equivalent types.
	 *
	 * @return the function builder
	 */
	private StringBuilder generateFunctions(boolean performTypesConversion) {
		var builder = new StringBuilder();
		
		BuilderUtils.readFileLines(FunctionsBuilder.C_FUNCTIONS_PATH, line -> {
			line = performTypeConversion(line);
			
			/* Perform types conversion */
			if (performTypesConversion) {
				/* Replaces types from type dictionary */
				line = BuilderUtils.replaceTypes(conversionTypes, line);
				/* Fetch unsupported types that are not yet in the global list */
				unsupportedConversionTypes.addAll(getUnsupportedTypes(line, conversionTypes).stream().filter(type -> !unsupportedConversionTypes.contains(type)).toList());
			}
			
			/* Perform typedef conversion */
			line = BuilderUtils.replaceTypes(conversionTypedefs, line);
			unsupportedConversionTypedefs.addAll(getUnsupportedTypes(line, conversionTypedefs).stream().filter(type -> !unsupportedConversionTypedefs.contains(type)).toList());
			
			/* Perform equivalent type conversion */
			line = BuilderUtils.replaceTypes(equivalentTypes, line);
			unsupportedEquivalentTypes.addAll(getUnsupportedTypes(line, equivalentTypes).stream().filter(type -> !unsupportedEquivalentTypes.contains(type)).toList());
			
			builder.append(BuilderUtils.formattingLine(line, "", "\n"));
		});
		return builder;
	}
	
	
}