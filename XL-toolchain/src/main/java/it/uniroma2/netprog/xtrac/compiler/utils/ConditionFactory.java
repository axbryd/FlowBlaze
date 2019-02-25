package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.opcodes.ConditionOpcode;
import it.uniroma2.netprog.xtrac.model.operand.Operand;
import java.util.LinkedHashMap;
import java.util.Vector;

public class ConditionFactory {
    private static ConditionFactory ourInstance = new ConditionFactory();
    private int counter;
    private LinkedHashMap<Condition, Integer> conditions;

    public static ConditionFactory getInstance() {
        return ourInstance;
    }

    private void resetInstance() {
        ourInstance = new ConditionFactory();
    }

    private ConditionFactory() {
        counter = 0;
        conditions = new LinkedHashMap<>();
    }

    public Condition createCondition(Operand op0, Operand op1, ConditionOpcode opcode) {
        Condition tempCondition = new Condition(counter, op0, op1, opcode);
        conditions.put(tempCondition, counter);
        counter++;
        return tempCondition;
    }

    public boolean isPresent(Operand op0, Operand op1, ConditionOpcode opcode) {
        Condition tempCondition = new Condition(counter, op0, op1, opcode);
        return conditions.containsKey(tempCondition);
    }

    public Condition getCondition(Operand op0, Operand op1, ConditionOpcode opcode) {
        Condition tempCondition = new Condition(counter, op0, op1, opcode);
        tempCondition.setId(conditions.get(tempCondition));
        return tempCondition;
    }

    public Vector<Condition> getConditionsAndClear() {
        Vector<Condition> conditions = new Vector<>(this.conditions.keySet());
        resetInstance();
        return conditions;
    }
}
