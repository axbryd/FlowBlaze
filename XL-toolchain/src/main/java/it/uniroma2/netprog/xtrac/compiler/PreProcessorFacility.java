package it.uniroma2.netprog.xtrac.compiler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class PreProcessorFacility {
    private static PreProcessorFacility ourInstance = new PreProcessorFacility();

    private Vector<Vector<String>> filesToSubstitute;

    private HashMap<String, String> constantsToSubstitute;

    public static PreProcessorFacility getInstance() {
        return ourInstance;
    }

    private PreProcessorFacility() {
        filesToSubstitute = new Vector<>();
        constantsToSubstitute = new HashMap<>();
    }

    public void addFileToImport(Vector<String> files) {
        filesToSubstitute.add(files);
    }

    public void addConstant(String name, String value) {
        constantsToSubstitute.put(name, value);
    }

    public void preProcessFile(String filename) throws IOException {
        try {
            // preprocessing file input
            String[] directoryPathSplit = filename.split("/");
            String directoryPath = directoryPathSplit.length > 1 ? filename.replace(directoryPathSplit[directoryPathSplit.length-1], "") : "";

            Charset charset = StandardCharsets.UTF_8;
            Path originalFilePath = Paths.get(filename);
            String originalFile = new String(Files.readAllBytes(originalFilePath), charset);

            // TODO: Multiple level import
            for(Vector<String> groupOfFileToSubstitute: filesToSubstitute) {
                String stringToSubstituteWith = new String();
                for(String file: groupOfFileToSubstitute) {

                    Path fileToInsertPath = Paths.get(directoryPath+file);
                    stringToSubstituteWith += new String(Files.readAllBytes(fileToInsertPath), charset);
                }
               originalFile = originalFile.replaceFirst("(import)([^;]*)(;)", stringToSubstituteWith);
            }

            // preprocessing constants
            originalFile = originalFile.replaceAll("(Constant)([^;]*)(;)", "");

            Set<String> constants = constantsToSubstitute.keySet();
            for(String constant: constants) {
                originalFile = originalFile.replace(constant, constantsToSubstitute.get(constant));
            }

            Path newFilePath = Paths.get(filename+"i");
            Files.write(newFilePath, originalFile.getBytes(charset));
        } catch (IOException e) {
            throw new IOException("Unable to open source file");
        }
    }
}
