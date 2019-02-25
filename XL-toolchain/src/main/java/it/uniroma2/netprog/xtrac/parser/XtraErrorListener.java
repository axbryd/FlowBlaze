package it.uniroma2.netprog.xtrac.parser;

import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class XtraErrorListener extends ConsoleErrorListener{
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        System.err.print("[XTRAC] parsing found a syntax error:\n\t\t");
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        System.exit(1);
    }
}
