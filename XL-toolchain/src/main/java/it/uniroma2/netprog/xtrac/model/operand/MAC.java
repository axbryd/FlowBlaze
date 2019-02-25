package it.uniroma2.netprog.xtrac.model.operand;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MAC extends AddressConstant {
    public MAC(String value) {
        super(value);
    }
}
