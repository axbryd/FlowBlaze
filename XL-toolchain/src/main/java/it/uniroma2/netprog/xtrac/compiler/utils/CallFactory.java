package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.InvalidValueException;
import it.uniroma2.netprog.xtrac.exception.TooManyArgsException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredRegisterException;
import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.action.HardwareAction;
import it.uniroma2.netprog.xtrac.model.action.StageTransition;
import it.uniroma2.netprog.xtrac.model.action.StateTransition;
import it.uniroma2.netprog.xtrac.model.opcodes.*;
import it.uniroma2.netprog.xtrac.model.operand.Operand;
import it.uniroma2.netprog.xtrac.model.operand.PacketField;
import it.uniroma2.netprog.xtrac.model.stage.FlowKey;
import it.uniroma2.netprog.xtrac.model.stage.UpdateKey;
import it.uniroma2.netprog.xtrac.parser.xtraParser;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class CallFactory {
    private HashMap<Opcode, Integer> argsMap= new HashMap<>();

    private static CallFactory ourInstance = new CallFactory();

    public static CallFactory getInstance() {
        return ourInstance;
    }

    private CallFactory() {
        argsMap.put(HardwareActionOpcode.XTRA_SENDPKT, 2);
        argsMap.put(HardwareActionOpcode.XTRA_SETTIMER, 3);
        argsMap.put(StateTransitionOpcode.XTRA_STATE_TRANSITION, 1);
        argsMap.put(HardwareActionOpcode.XTRA_STOREPKT, 1);
        argsMap.put(HardwareActionOpcode.XTRA_SETFIELD, 3);
        argsMap.put(HardwareActionOpcode.XTRA_DELETEPKT, 1);
        argsMap.put(StageTransitionOpcode.XTRA_STAGE_TRANSITION, 1);
        argsMap.put(HardwareActionOpcode.XTRA_DELETE_INSTANCE, 0);
    }

    public StateTransition getStateTransition(List<xtraParser.TermContext> terms) throws TooManyArgsException {
            if (terms.size() != argsMap.get(StateTransitionOpcode.XTRA_STATE_TRANSITION))
                throw new TooManyArgsException("Too many args for the function 'setNextState', got: "+terms.size()+
                        ", expected: "+argsMap.get(StateTransitionOpcode.XTRA_STATE_TRANSITION));
            return new StateTransition(StateFactory.getInstance().referenceState(terms.get(0).getText()));
    }

    public StageTransition getStageTransition(List<xtraParser.TermContext> terms) throws TooManyArgsException {
        if (terms.size() != argsMap.get(StageTransitionOpcode.XTRA_STAGE_TRANSITION))
            throw new TooManyArgsException("Too many args for the function 'setNextStage', got: "+terms.size()+
                    ", expected: "+argsMap.get(StageTransitionOpcode.XTRA_STAGE_TRANSITION));
        return new StageTransition(StageFactory.getInstance().referenceStage(terms.get(0).getText()));
    }

    public FlowKey getFlowKey(List<xtraParser.TermContext> terms) throws InvalidValueException, UndeclaredRegisterException, UnknownOpcodeException {
        Vector<PacketField> packetFields = new Vector<>();
        for(xtraParser.TermContext term: terms) {
            Operand operand;
            if(term.index_access() != null || term.field_elem() != null) {
                operand = OperandFactory.getInstance().getOperand(term);
            }
            else {
                throw new InvalidValueException("Unable to use setFlowKey, with the parameter '"+term.getText()+"'");
            }
            packetFields.add((PacketField) operand);
        }
        return new FlowKey(packetFields);
    }

    public UpdateKey getUpdateKey(List<xtraParser.TermContext> terms) throws InvalidValueException, UndeclaredRegisterException, UnknownOpcodeException {
        Vector<PacketField> packetFields = new Vector<>();
        for(xtraParser.TermContext term: terms) {
            Operand operand;
            if(term.index_access() != null || term.field_elem() != null) {
                operand = OperandFactory.getInstance().getOperand(term);
            }
            else {
                throw new InvalidValueException("Unable to use setUpdateKey, with the parameter '"+term.getText()+"'");
            }
            packetFields.add((PacketField) operand);
        }
        return new UpdateKey(packetFields);
    }

    public boolean isASetFlowKey(String name) {
        return name.equals("setFlowKey");
    }

    public boolean isASetUpdateKey(String name) {
        return name.equals("setUpdateKey");
    }

    public boolean isAStateTransition(String name)
    {
        return name.equals("setNextState");
    }

    public boolean isAStageTransition(String name)
    {
        return name.equals("setNextStage");
    }

    public Action getAction(String name, Vector<Operand> args) throws TooManyArgsException, UnknownOpcodeException {

        HardwareActionOpcode opcode = null;
        opcode = HardwareActionOpcode.toHardwareActionOpcode(name); // is it an hardware action?

        if (args.size() != argsMap.get(opcode))
            throw new TooManyArgsException("Too many args for the function '" + name + "', got: " + args.size() + ", expected: " + argsMap.get(opcode));
        switch (argsMap.get(opcode)) {
            case 0:
                return new HardwareAction(opcode);
            case 1:
                return new HardwareAction(args.get(0), opcode);

            case 2:
                return new HardwareAction(args.get(0), args.get(1), opcode);

            case 3:
                return new HardwareAction(args.get(0), args.get(1), args.get(2), opcode);

            default:
                return null;
        }
    }
}