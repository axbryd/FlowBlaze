package it.uniroma2.netprog.xtrac.compiler;

import it.uniroma2.netprog.xtrac.parser.xtraBaseListener;
import it.uniroma2.netprog.xtrac.parser.xtraParser;
import java.util.Vector;

public class XtraPreProcessor extends xtraBaseListener {
    // -----------------------------------------------------------------------------------------------------------------
    // File import

    @Override
    public void exitFile_import(xtraParser.File_importContext ctx) {
        Vector<String> files = new Vector<>();
        for(xtraParser.File_nameContext file_nameContext: ctx.file_name()) {
            String fileName= file_nameContext.FILENAME().getText();
            files.add(fileName.substring(1, fileName.length()-1));
        }
        PreProcessorFacility.getInstance().addFileToImport(files);
    }

    @Override
    public void exitConst_assignement(xtraParser.Const_assignementContext ctx) {
        PreProcessorFacility.getInstance().addConstant(ctx.identifier().getText(),
                ctx.addr() != null ? ctx.addr().getText() : ctx.integer().getText());
    }
}
