package function.builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HeaderBuilder {
    private static final String[] ADDITIONAL_DEFINITIONS = {"ADDITIONAL_DEFINITION", "REPLACE_DEFINITION"};

    public static void main(String[] args) {
        if (args.length > 0) {
            build(args[0]);
        } else {
            build("/usr/local/include/meos.h");
        }
    }

    public static void build(String headerPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(headerPath));
             FileWriter writer = new FileWriter("pymeos_cffi/builder/meos.h")) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Remove comments
                line = line.replaceAll("//.*", "");
                line = line.replaceAll("/\\*.*?\\*/", "");
                // Comment macros
                line = line.replace("#", "//#");
                line = line.replaceAll("^//(\\#define \\w+ \\d+)\\s*$", "$1");
                // Add additional definitions
                line = line.replace(ADDITIONAL_DEFINITIONS[0], ADDITIONAL_DEFINITIONS[1]);
                contentBuilder.append(line).append(System.lineSeparator());
            }
            writer.write(contentBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
