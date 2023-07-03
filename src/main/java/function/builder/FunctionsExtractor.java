package function.builder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe permettant d'extraire les fonctions de la librairie MEOS.
 *
 * @author Killian Monnier
 * @since 27/06/2023
 */
public class FunctionsExtractor {
    private static final String FILE_PATH = "src/main/java/function/builder/";
    private static final String INPUT_FILE_PATH = FILE_PATH + "meos.h";
    private static final String OUTPUT_FILE_PATH = FILE_PATH + "function.tmp";

    /**
     * Modèle RegEx de reconnaissance d'une fonction dans le fichier meos.h
     */
    private static final String FUNCTION_PATTERN = "extern (static inline )?[a-zA-Z0-9_]+ [a-zA-Z0-9_*]+\\([a-zA-Z0-9_* ,]+\\);";

    /**
     * Fonction de lancement du script.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        List<String> functions = extractFunctionsFromFile(INPUT_FILE_PATH);
        writeFunctionsToFile(functions, OUTPUT_FILE_PATH);
        System.out.println("Extraction des fonctions terminée. Les fonctions ont été écrites dans le fichier " + OUTPUT_FILE_PATH + ".");
    }

    /**
     * Extrait les fonctions depuis le fichier.
     *
     * @param filePath chemin du fichier
     * @return liste des fonctions extraites
     */
    public static List<String> extractFunctionsFromFile(String filePath) {
        List<String> functions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Pattern pattern = Pattern.compile(FUNCTION_PATTERN);

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    functions.add(matcher.group());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return functions;
    }

    /**
     * Écrit les fonctions dans un fichier.
     *
     * @param functions la liste des fonctions extraites
     * @param filePath  le chemin du fichier
     */
    public static void writeFunctionsToFile(List<String> functions, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String function : functions) {
                writer.write(function);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
