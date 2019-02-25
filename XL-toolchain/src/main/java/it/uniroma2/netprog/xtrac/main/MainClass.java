package it.uniroma2.netprog.xtrac.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.uniroma2.netprog.xtrac.compiler.PreProcessorFacility;
import it.uniroma2.netprog.xtrac.compiler.XtraCompiler;
import it.uniroma2.netprog.xtrac.compiler.XtraPreProcessor;
import it.uniroma2.netprog.xtrac.compiler.utils.ProgramFactory;
import it.uniroma2.netprog.xtrac.model.program.Program;
import it.uniroma2.netprog.xtrac.parser.XtraErrorListener;
import it.uniroma2.netprog.xtrac.parser.xtraBaseListener;
import it.uniroma2.netprog.xtrac.parser.xtraLexer;
import it.uniroma2.netprog.xtrac.parser.xtraParser;
import it.uniroma2.netprog.xtrac.serializers.ProgramSerializer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.cli.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainClass {
    public static void main(final String[] args) {
        // -------------------------------------------------------------------------------------------------------------
        // Parsing command line args

        Options options = new Options();
        Option option = Option.builder("i")
                .argName("file")
                .hasArg()
                .required()
                .longOpt("input")
                .desc("input file")
                .build();

        options.addOption(option);

        option = Option.builder("o")
                .argName("file")
                .hasArg()
                .required()
                .longOpt("output")
                .desc("output json file")
                .build();

        options.addOption(option);


        CommandLineParser parserCmd = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parserCmd.parse(options, args);
        } catch (Exception e){
            help(options);

        }

        String inputFile = cmd.getOptionValue("i");
        String outputFile = cmd.getOptionValue("o");

        // -------------------------------------------------------------------------------------------------------------
        // File pre-processing
        try {
            preProcessFile(inputFile);
        } catch (IOException e) {
            System.err.println("[XTRAC] "+e.getMessage());
            System.exit(-1);
        }

        // -------------------------------------------------------------------------------------------------------------
        // Compiling

        xtraLexer lexer = null;
        try {
            lexer = new xtraLexer(CharStreams.fromFileName(inputFile+"i"));
        } catch (IOException e) {
            System.err.println("[XTRAC] Unable to find file "+ inputFile);
            System.exit(1);
        }
        lexer.removeErrorListeners();
        lexer.addErrorListener(new XtraErrorListener());

        xtraParser parser = new xtraParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new XtraErrorListener());

        ParseTreeWalker walker = new ParseTreeWalker();
        XtraCompiler listener = new XtraCompiler();

        walk(walker, parser, listener);

        Program program = ProgramFactory.getInstance().getProgram();

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Program.class, new ProgramSerializer());
        gsonBuilder.setPrettyPrinting();
        final Gson gson = gsonBuilder.create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(gson.toJson(program));
            writer.close();
        } catch (IOException e) {
            System.err.println("[XTRAC] Unable to create file "+outputFile);
            System.exit(1);
        }
    }

    private static void walk(ParseTreeWalker walker, xtraParser parser, xtraBaseListener listener ) {
        try {
            walker.walk(listener, parser.program());
        }
        catch (ParseCancellationException e) {
            System.err.print("[XTRAC] Error while compiling: ");
            e.printStackTrace();
            System.out.println(e.getCause());
            System.exit(1);
        }
    }


    private static void preProcessFile(String inputFile) throws IOException {
        xtraLexer lexer = null;
        try {
            lexer = new xtraLexer(CharStreams.fromFileName(inputFile));
        } catch (IOException e) {
            System.err.println("[XTRAC] Unable to find file "+ inputFile);
            System.exit(1);
        }
        lexer.removeErrorListeners();
        lexer.addErrorListener(new XtraErrorListener());

        xtraParser parser = new xtraParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new XtraErrorListener());

        ParseTreeWalker walker = new ParseTreeWalker();
        XtraPreProcessor listener = new XtraPreProcessor();

        walk(walker, parser, listener);

        PreProcessorFacility.getInstance().preProcessFile(inputFile);
    }

    private static void help(Options options) {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("xtra", options);
        System.exit(-1);
    }
}
