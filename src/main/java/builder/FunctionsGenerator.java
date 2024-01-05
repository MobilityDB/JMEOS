package builder;

import utils.BuilderUtils;
import utils.Pair;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to generate the functions from the MEOS library.
 * Run with ./script folder
 *
 * @author Killian Monnier and Nidhal Mareghni
 * @since 27/06/2023
 */
public class FunctionsGenerator {
	
	/**
	 * Path of the file generated by {@link FunctionsExtractor}. Contains a list of functions signature.
	 */
	private final Path C_functionsPath;
	
	/**
	 * Path of the file generated by {@link FunctionsExtractor}. Contains a list of types definition.
	 */
	@SuppressWarnings("FieldCanBeLocal")
	private final Path C_typesPath;
	
	/**
	 * Path of the file generated by this class.
	 */
	private final Path functionsFilePath;

	/**
	 * Types dictionary. Contains the C types and there equivalent in Java
	 */
	private final HashMap<String, String> equivalentTypes = buildEquivalentTypes();
	

	private final HashMap<String, String> conversionTypes = buildConversionTypes();
	

	private final HashMap<String, String> conversionTypedefs;


	private final ArrayList<String> unsupportedEquivalentTypes = new ArrayList<>(); // List of unsupported types
	private final ArrayList<String> unsupportedConversionTypedefs = new ArrayList<>(); // List of unsupported types
	private final ArrayList<String> unsupportedConversionTypes = new ArrayList<>(); // List of unsupported types
	
	/**
	 * Constructor of {@link FunctionsExtractor}.
	 *
	 * @throws URISyntaxException thrown when resources not found
	 */
	public FunctionsGenerator() throws URISyntaxException {
		this.C_functionsPath = Paths.get(Objects.requireNonNull(this.getClass().getResource("meos_functions.h")).toURI());
		this.C_typesPath = Paths.get(Objects.requireNonNull(this.getClass().getResource("meos_types.h")).toURI());
		this.functionsFilePath = Paths.get(new URI(Objects.requireNonNull(this.getClass().getResource("")) + "functions.java"));
		this.conversionTypedefs = this.buildConversionTypedefs(this.C_typesPath.toString());
	}
	
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
	 * Launch process of extraction.
	 *
	 * @param args arguments
	 * @throws URISyntaxException thrown when resources not found
	 */
	public static void main(String[] args) throws URISyntaxException {
		
		var generator = new FunctionsGenerator();
		
		/* Generation of all the functions signature */
		StringBuilder functionsInterfaceBuilder = generator.generateFunctions(generator.C_functionsPath.toString(), false);
		StringBuilder functionsClassBuilder = generator.generateFunctions(generator.C_functionsPath.toString(), true);
		System.out.println("Unsupported types: " + generator.unsupportedEquivalentTypes);
		System.out.println("Unsupported conversion typedefs: " + generator.unsupportedConversionTypedefs);
		System.out.println("Unsupported conversion types: " + generator.unsupportedConversionTypes);
		
		/* Generation of the file */
		StringBuilder interfaceBuilder = generator.generateInterface(functionsInterfaceBuilder);
		StringBuilder classBuilder = generator.generateClass(functionsClassBuilder, interfaceBuilder);
		BuilderUtils.writeFileFromBuilder(classBuilder, generator.functionsFilePath.toString());
		String tmp_functionsFilePath = Paths.get("").toAbsolutePath() + "/src/main/java/functions/functions.java";
		BuilderUtils.writeFileFromBuilder(classBuilder, tmp_functionsFilePath);
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
			line = line.replaceAll("char\\s\\*\\*", "**char ");
			line = line.replaceAll("char\\s\\*", "*char ");
			line = line.replaceAll("\\w+\\s\\*\\*", "*[] ");
			line = line.replaceAll("\\w+\\s\\*(?!\\*)", "* ");
			
			/* Changing special types or names */
			line = line.replaceAll("\\(void\\)", "()"); // Remove the void parameter (for the function meos_finish(void)) //
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
		types.put("\\*\\*char", "Pointer");
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
		//types.put("\\char **","Pointer");
		
		return types;
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
	 * Build the dictionary of typedef conversion.
	 * <pre>
	 * Key: old type in C or modified
	 * Value: new type in Java
	 * </pre>
	 *
	 * @param filePath file path of C typedefs
	 * @return types dictionary
	 */
	private HashMap<String, String> buildConversionTypedefs(String filePath) {
		HashMap<String, String> typedefs = new HashMap<>();
		
		/* Added typedefs extracted from C file */
		BuilderUtils.readFileLines(filePath, line -> {
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
		if (!typesNamesList.isEmpty()) {
			/* Modify the names of the parameter types that has endured conversion */
			for (var typeNames : typesNamesList) {
				String oldName = typeNames.key();
				String newName = typeNames.value();
				paramNames = BuilderUtils.modifyList(paramNames, oldName, newName);
			}
		}
		if (! (paramNames.contains("result") || paramNames.contains("size_out"))){
			functionCallingProcess.add("MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
		}

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
				if (paramNames.contains("result")){
					functionCallingProcess.add("boolean out;");
					functionCallingProcess.add("Runtime runtime = Runtime.getSystemRuntime();");
					int total = signature.split(Pattern.quote("Pointer"), -1).length -1;
					if (total > 1){
						functionCallingProcess.add("Pointer result = Memory.allocateDirect(runtime, Long.BYTES);");
						functionCallingProcess.add("out = MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
						functionCallingProcess.add("Pointer new_result = result.getPointer(0);");
						functionCallingProcess.add("return out ? new_result : null ;");
					}
					else if (signature.contains("int ")){
						functionCallingProcess.add("Pointer result = Memory.allocateDirect(runtime, Integer.BYTES);");
						functionCallingProcess.add("out = MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
						functionCallingProcess.add("return out ? result.getInt(0) : null ;");
					}
					else if (signature.contains("double ")){
						functionCallingProcess.add("Pointer result = Memory.allocateDirect(runtime, Double.BYTES);");
						functionCallingProcess.add("out = MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
						functionCallingProcess.add("return out ? result.getDouble(0) : null ;");
					}
					else if (signature.contains("long ")){
						functionCallingProcess.add("Pointer result = Memory.allocateDirect(runtime, Long.BYTES);");
						functionCallingProcess.add("out = MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
						functionCallingProcess.add("return out ? result.getLong(0) : null ;");
					}

					else if (signature.contains("long ")){
						functionCallingProcess.add("Pointer result = Memory.allocateDirect(runtime, Long.BYTES);");
						functionCallingProcess.add("out = MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
						functionCallingProcess.add("return out ? result.getLong(0) : null ;");
					}

					else if (signature.contains("boolean ")){
						functionCallingProcess.add("Pointer result = Memory.allocateDirect(runtime, Long.BYTES);");
						functionCallingProcess.add("out = MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");
						functionCallingProcess.add("return out ? true : false ;");
					}

				}
				else if (paramNames.contains("size_out")){
					functionCallingProcess.add("Runtime runtime = Runtime.getSystemRuntime();");
					functionCallingProcess.add("Pointer size_out = Memory.allocateDirect(runtime, Long.BYTES);");
					functionCallingProcess.add("return MeosLibrary.meos." + BuilderUtils.extractFunctionName(signature) + "(" + BuilderUtils.getListWithoutBrackets(paramNames) + ");");

				}
				else{
					functionCallingProcess.set(0, "return " + functionCallingProcess.get(0));
				}

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
		
		String imports_and_package = """
				package functions;
							
				import jnr.ffi.Pointer;
				import jnr.ffi.Memory;
				import jnr.ffi.Runtime;
				import utils.JarLibraryLoader;
				import utils.meosCatalog.MeosEnums.meosType;
				import utils.meosCatalog.MeosEnums.meosOper;
								
				import java.time.*;
				""";
		String className = "public class functions {";
		builder.append(imports_and_package).append("\n").append(className).append("\n");
		
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

				String functionSignature2 =  "public static " + BuilderUtils.removeSemicolon(line) + " {\n";
				if (functionSignature2.contains("result")){
					int total = functionSignature2.split(Pattern.quote("Pointer"), -1).length -1;
					if (total > 1){
						functionSignature2 = functionSignature2.replace("public static boolean","public static Pointer");
					}
					else if (functionSignature2.contains("int ")){
						functionSignature2 = functionSignature2.replace("public static boolean","public static int");
					}
					else if (functionSignature2.contains("double ")){
						functionSignature2 = functionSignature2.replace("public static boolean","public static double");
					}
					else if (functionSignature2.contains("long ")){
						functionSignature2 = functionSignature2.replace("public static boolean","public static long");
					}
					functionSignature2 = functionSignature2.replace(", Pointer result","");
				}

				else if (functionSignature2.contains("as_hexwkb") || functionSignature2.contains("as_wkb")){
					functionSignature2 = functionSignature2.replace(", Pointer size_out","");
				}
				/* Add all the different parts in the function body builder */
				functionBodyBuilder.append("@SuppressWarnings(\"unused\")\n")
						.append(functionSignature2)
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
	 * @param filePath               file path of C functions
	 * @param performTypesConversion true if it needs to perform a types conversion using {@link FunctionsGenerator#conversionTypes}
	 * @return the function builder
	 */
	private StringBuilder generateFunctions(String filePath, boolean performTypesConversion) {
		var builder = new StringBuilder();
		
		BuilderUtils.readFileLines(filePath, line -> {
			line = performTypeConversion(line);
			
			/* Perform types conversion */
			if (performTypesConversion) {
				line = BuilderUtils.replaceTypes(conversionTypes, line);
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