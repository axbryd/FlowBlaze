package it.uniroma2.netprog.xtrac.model.table;

import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.state.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Vector;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    private State initialState;
    private Vector<TableRow> tableRows;
    private Vector<Condition> conditions;
}
