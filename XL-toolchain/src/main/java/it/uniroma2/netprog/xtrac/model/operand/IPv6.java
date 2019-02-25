package it.uniroma2.netprog.xtrac.model.operand;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPv6 extends AddressConstant {
    public IPv6(String value) {
        super(value);
    }
}
