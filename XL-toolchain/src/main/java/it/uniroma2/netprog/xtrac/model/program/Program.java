package it.uniroma2.netprog.xtrac.model.program;

import it.uniroma2.netprog.xtrac.model.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Vector;

@Getter
@Setter
@AllArgsConstructor
public class Program {
    private Vector<Stage> stages;
}
