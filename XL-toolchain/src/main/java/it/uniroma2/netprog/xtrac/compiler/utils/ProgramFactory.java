package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.model.program.Program;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramFactory {
    private static ProgramFactory ourInstance = new ProgramFactory();

    private Program program;

    public static ProgramFactory getInstance() {
        return ourInstance;
    }

    private ProgramFactory() {
    }
}
