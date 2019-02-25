package it.uniroma2.netprog.xtrac.model.operand;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PacketField implements Operand {
    String name;
    String from;
    String length;

    @Override
    public String toString() {
        return name;
    }
  }
