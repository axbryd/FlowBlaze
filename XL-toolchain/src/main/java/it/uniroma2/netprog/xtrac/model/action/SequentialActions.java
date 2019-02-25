package it.uniroma2.netprog.xtrac.model.action;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Vector;

@NoArgsConstructor
@Getter
@Setter
public class SequentialActions extends Action {
    Vector<SequentializableActions> actions = new Vector<>();
}
