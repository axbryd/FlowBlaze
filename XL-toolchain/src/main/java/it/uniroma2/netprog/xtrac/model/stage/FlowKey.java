package it.uniroma2.netprog.xtrac.model.stage;

import it.uniroma2.netprog.xtrac.model.operand.PacketField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Vector;

@Getter
@Setter
@AllArgsConstructor
public class FlowKey {
    private Vector<PacketField> headerFields;
}
