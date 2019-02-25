package it.uniroma2.netprog.xtrac.model.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConditionResult {
    private Condition id;
    private Boolean result;
}
