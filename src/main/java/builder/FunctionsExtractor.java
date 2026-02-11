package builder;

import utils.BuilderUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to extract the functions from the MEOS library.
 * UPDATED VERSION to handle multiple .h files (meos.h + meos_geo.h)
 *
 * Run with directly with java through the main class
 *
 * @author Killian Monnier (original)
 * @author Updated for MEOS 1.3+ (multi-file support)
 * @since 27/06/2023
 */
public class FunctionsExtractor {
	private static final String FUNCTION_PATTERN = "(extern\\s+(static\\s+|inline\\s+|const\\s+)?|static\\s+|inline\\s+|const\\s+)?[a-zA-Z0-9_*]+\\s*\\**\\s+[a-zA-Z0-9_*]+\\s*\\([^)]*\\);";
	private static final String TYPES_PATTERN = "typedef\\s(?!struct|enum)\\w+\\s\\w+;";

	// Chemins des fichiers d'entrée
	private Path inputMeosPath = null;
	private Path inputMeosGeoPath = null;

	// Chemins des fichiers de sortie
	private Path outputFunctionsFilePath = null;
	private Path outputTypesFilePath = null;

	String currentDir = System.getProperty("user.dir");

	/**
	 * Constructor of {@link FunctionsExtractor}.
	 *
	 * @throws URISyntaxException thrown when resources not found
	 */
	public FunctionsExtractor() throws URISyntaxException {
		// Chemins des fichiers d'entrée (à jour)
		String reqDirMeos = "src/main/java/builder/resources/meos.h";
		String reqDirMeosGeo = "src/main/java/builder/resources/meos_geo.h";

		this.inputMeosPath = Paths.get(currentDir, reqDirMeos);
		this.inputMeosGeoPath = Paths.get(currentDir, reqDirMeosGeo);

		// Chemins des fichiers de sortie
		this.outputFunctionsFilePath = Paths.get(new URI(Objects.requireNonNull(this.getClass().getResource("")) + "meos_functions.h"));
		this.outputTypesFilePath = Paths.get(new URI(Objects.requireNonNull(this.getClass().getResource("")) + "meos_types.h"));
	}

	/**
	 * Launch process of extraction.
	 *
	 * @param args arguments
	 * @throws URISyntaxException thrown when resources not found
	 */
	public static void main(String[] args) throws URISyntaxException {
		var extractor = new FunctionsExtractor();

		System.out.println("=== MEOS Functions Extractor v2.0 ===");
		System.out.println("Extracting from multiple header files...\n");

		// Extraction des fonctions depuis meos.h
		System.out.println("1. Extracting functions from meos.h...");
		var functionsFromMeos = BuilderUtils.extractPatternFromFile(
				extractor.inputMeosPath.toString(),
				FUNCTION_PATTERN
		);
		System.out.println("   → Found " + functionsFromMeos.size() + " functions in meos.h");

		// Extraction des fonctions depuis meos_geo.h
		System.out.println("2. Extracting functions from meos_geo.h...");
		var functionsFromMeosGeo = BuilderUtils.extractPatternFromFile(
				extractor.inputMeosGeoPath.toString(),
				FUNCTION_PATTERN
		);
		System.out.println("   → Found " + functionsFromMeosGeo.size() + " functions in meos_geo.h");

		// Combinaison des deux listes (sans doublons)
		List<String> allFunctions = new ArrayList<>(functionsFromMeos);
		for (String func : functionsFromMeosGeo) {
			if (!allFunctions.contains(func)) {
				allFunctions.add(func);
			}
		}
		System.out.println("3. Combined total: " + allFunctions.size() + " unique functions\n");

		// Écriture du fichier de sortie
		BuilderUtils.writeFileFromArray(allFunctions, extractor.outputFunctionsFilePath.toString());
		System.out.println("✓ Functions written to: " + extractor.outputFunctionsFilePath);

		// Extraction des types depuis meos.h
		System.out.println("\n4. Extracting types from meos.h...");
		var types = extractor.getTypesFromFile(extractor.inputMeosPath.toString());
		System.out.println("   → Found " + types.size() + " types");

		BuilderUtils.writeFileFromArray(types, extractor.outputTypesFilePath.toString());
		System.out.println("✓ Types written to: " + extractor.outputTypesFilePath);

		System.out.println("\n=== Extraction completed successfully! ===");
	}

	/**
	 * Retrieves structure names from file.
	 *
	 * @param filePath file path
	 * @return list of structure names
	 */
	public static ArrayList<String> getStructureNames(String filePath) {
		List<String> structureNames = new ArrayList<>();
		try (var reader = new BufferedReader(new FileReader(filePath))) {
			var content = new StringBuilder();
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

	/**
	 * Get types from a file.
	 *
	 * @param filePath path to the header file
	 * @return the list of types
	 */
	private List<String> getTypesFromFile(String filePath) {
		var rawTypes = BuilderUtils.extractPatternFromFile(filePath, TYPES_PATTERN);
		var structureNames = getStructureNames(filePath);
		List<String> filteredTypes = new ArrayList<>();

		/* Add typedefs if they are not structure type */
		for (var rawType : rawTypes) {
			var words = rawType.trim().split("\\s+");
			if (words.length >= 2) {
				String typeName = words[1];
				if (!structureNames.contains(typeName)) {
					filteredTypes.add(rawType);
				}
			}
		}
		return filteredTypes;
	}
}