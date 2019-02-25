package it.uniroma2.netprog.xtrac.model.state;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class State {
    private int label;

    @Override
    public String toString() {
        return Integer.toString(label);
    }
}
