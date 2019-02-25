package it.uniroma2.netprog.xtrac.model.stage;

import it.uniroma2.netprog.xtrac.model.table.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stage {
    private Table table;
    private FlowKey flowKey;
    private UpdateKey updateKey;
    private int label;

    public Stage(int label) {
        this.label = label;
    }
}
