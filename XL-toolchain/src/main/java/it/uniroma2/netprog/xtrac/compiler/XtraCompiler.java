package it.uniroma2.netprog.xtrac.compiler;

import it.uniroma2.netprog.xtrac.compiler.utils.*;
import it.uniroma2.netprog.xtrac.compilerStateMachine.CompilerStateMachine;
import it.uniroma2.netprog.xtrac.exception.*;
import it.uniroma2.netprog.xtrac.model.action.Action;
import it.uniroma2.netprog.xtrac.model.action.Update;
import it.uniroma2.netprog.xtrac.model.condition.Boolean;
import it.uniroma2.netprog.xtrac.model.condition.Condition;
import it.uniroma2.netprog.xtrac.model.condition.ConditionResult;
import it.uniroma2.netprog.xtrac.model.event.Event;
import it.uniroma2.netprog.xtrac.model.opcodes.ConditionOpcode;
import it.uniroma2.netprog.xtrac.model.opcodes.UpdateOpcode;
import it.uniroma2.netprog.xtrac.model.operand.IntegerConstant;
import it.uniroma2.netprog.xtrac.model.operand.Operand;
import it.uniroma2.netprog.xtrac.model.operand.PacketField;
import it.uniroma2.netprog.xtrac.model.operand.Register;
import it.uniroma2.netprog.xtrac.model.program.Program;
import it.uniroma2.netprog.xtrac.model.stage.UpdateKey;
import it.uniroma2.netprog.xtrac.model.state.State;
import it.uniroma2.netprog.xtrac.parser.xtraBaseListener;
import it.uniroma2.netprog.xtrac.parser.xtraParser;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.List;
import java.util.Vector;

public class XtraCompiler extends xtraBaseListener {
    private Update update;

    // -----------------------------------------------------------------------------------------------------------------
    // Program

    @Override
    public void enterState_program(xtraParser.State_programContext ctx) {
        try {
            CompilerStateMachine.getInstance().setCurrentStageName("default");
            CompilerStateMachine.getInstance().goInsideBlock();
        } catch (IllegalCompilerTransitionException | AlreadyDefinedException e) {
            unknownError(e);
        }
    }

    @Override
    public void exitState_program(xtraParser.State_programContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitProgram(xtraParser.ProgramContext ctx) {
        try {
            ProgramFactory.getInstance().setProgram(new Program(StageFactory.getInstance().getStages()));
            StateFactory.getInstance().checkPendings();
        } catch (UndefinedFlowKeyException | UndeclaredStateException e) {
            compileError(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // PacketFields definition statements

    @Override
    public void exitPacket_field_definition(xtraParser.Packet_field_definitionContext ctx) {
        for (xtraParser.Packet_field_entryContext packet_field_entryContext : ctx.packet_field_entry()) {
            String fieldName = ctx.identifier().ID().getText() + "." + packet_field_entryContext.identifier().getText();

            try {
                PacketField packetField = PacketFieldFactory.getInstance().getPacketFieldFromIndexAccess(packet_field_entryContext.index_access().field_elem().getText(), packet_field_entryContext.index_access().INDEX().getText(), fieldName);
                PacketFieldFactory.getInstance().addPacketField(fieldName, packetField);
            } catch (InvalidValueException | UnknownOpcodeException e) {
                compileError(e);
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Register definition statements

    @Override
    public void exitRegister_definition(xtraParser.Register_definitionContext ctx) {
        List<xtraParser.IdentifierContext> identifierContexts = ctx.identifier();
        if (ctx.GLOBAL_MODIFIER() != null) {
            for (xtraParser.IdentifierContext identifierContext : identifierContexts) {
                try {
                    GlobalRegisterFactory.getInstance().allocateRegister(identifierContext.ID().getText());
                } catch (AlreadyDefinedException e) {
                    compileError(e);
                }
            }
        } else {
            for (xtraParser.IdentifierContext identifierContext : identifierContexts) {
                try {
                    LocalRegisterFactory.getInstance().allocateRegister(identifierContext.ID().getText());
                } catch (AlreadyDefinedException e) {
                    compileError(e);
                }
            }
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // MacroAction definition statements

    @Override
    public void enterMacro_action_definition(xtraParser.Macro_action_definitionContext ctx) {
        try {
            CompilerStateMachine.getInstance().setCurrentMacroAction(ctx.identifier().ID().getText());
            CompilerStateMachine.getInstance().goInsideActionsBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitMacro_action_definition(xtraParser.Macro_action_definitionContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
             compileError(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Stage statements

    @Override
    public void enterStage_statement(xtraParser.Stage_statementContext ctx) {
        try {
            CompilerStateMachine.getInstance().setCurrentStageName(ctx.identifier().ID().getText());
            CompilerStateMachine.getInstance().goInsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitStage_statement(xtraParser.Stage_statementContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
             compileError(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // State statements

    @Override
    public void enterState_statement(xtraParser.State_statementContext ctx) {
        State state = StateFactory.getInstance().declareState(ctx.identifier().ID().getText());

        try {
            CompilerStateMachine.getInstance().setCurrentState(state);
            CompilerStateMachine.getInstance().setInitial(ctx.INITIAL_MODIFIER() != null);

            CompilerStateMachine.getInstance().goInsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitState_statement(xtraParser.State_statementContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
            compileError(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Event statements

    @Override
    public void enterEvent_statement(xtraParser.Event_statementContext ctx) {
        Event event = null;
        try {
            event = Event.toEvent(ctx.identifier().ID().getText());
        } catch (UnknownOpcodeException e) {
            compileError(e);
        }

        if (event == null) {
            // TODO: implement custom events
            System.err.println("[XTRAC] undefined event '" + ctx.identifier().ID().getText() + "'");
            System.exit(-1);
        }
        try {
            CompilerStateMachine.getInstance().setCurrentEvent(event);
            CompilerStateMachine.getInstance().goInsideBlock();
        } catch (IllegalCompilerTransitionException | AlreadyDefinedException e) {
            unknownError(e);
        }
    }

    @Override
    public void exitEvent_statement(xtraParser.Event_statementContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
            compileError(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Action statements

    @Override
    public void enterSerial(xtraParser.SerialContext ctx) {
        try {
            CompilerStateMachine.getInstance().goInsideActionsBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitSerial(xtraParser.SerialContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitExpr(xtraParser.ExprContext ctx) {
        xtraParser.TermContext op0Ctx = ctx.term(0);
        xtraParser.TermContext op1Ctx = ctx.term(1);
        Operand op0, op1;
        UpdateOpcode opcode = null;

        if (op1Ctx != null) {
            if (ctx.PLUS() != null)
                opcode = UpdateOpcode.XTRA_SUM;
            else if (ctx.MINUS() != null)
                opcode = UpdateOpcode.XTRA_MINUS;
            else if (ctx.MUL() != null)
                opcode = UpdateOpcode.XTRA_MULTIPLY;
            else if (ctx.DIV() != null)
                opcode = UpdateOpcode.XTRA_DIVIDE;
            else if (ctx.MAX() != null)
                opcode = UpdateOpcode.XTRA_MAX;
            else if (ctx.MIN() != null)
                opcode = UpdateOpcode.XTRA_MIN;

            try {
                op0 = OperandFactory.getInstance().getOperand(op0Ctx);
                op1 = OperandFactory.getInstance().getOperand(op1Ctx);
                update = new Update(opcode, op0, op1, null); // dest register will be written by the declaration
            } catch (UndeclaredRegisterException | InvalidValueException | UnknownOpcodeException e) {
                compileError(e);
            }
        } else {
            try {
                op0 = OperandFactory.getInstance().getOperand(op0Ctx);
                op1 = new IntegerConstant("0");
                update = new Update(UpdateOpcode.XTRA_SUM, op0, op1, null); // dest register will be written by the declaration
            } catch (UndeclaredRegisterException | InvalidValueException | UnknownOpcodeException e) {
                compileError(e);
            }
        }
    }

    @Override
    public void exitAssignment(xtraParser.AssignmentContext ctx) {
        String registerName = ctx.identifier().getText();
        Register register = null;
        try {
            register = OperandFactory.getInstance().getRegister(registerName);
        } catch (UndeclaredRegisterException e) {
            compileError(e);
        }

        update.setOp2(register);

        CompilerStateMachine.getInstance().addAction(update);
    }

    @Override
    public void exitCall(xtraParser.CallContext ctx) {
        String functionName = ctx.ID().getSymbol().getText();
        List<xtraParser.TermContext> terms = ctx.term();

        Action action = null;

        if (CallFactory.getInstance().isAStateTransition(functionName)) {
            try {
                action = CallFactory.getInstance().getStateTransition(terms);
                CompilerStateMachine.getInstance().addAction(action);
            } catch (TooManyArgsException e) {
                compileError(e);
            }
        } else if (CallFactory.getInstance().isASetFlowKey(functionName)) {
            try {
                // TODO: check illegal sets of flowkey
                StageFactory.getInstance().getLastDeclaredStage().setFlowKey(CallFactory.getInstance().getFlowKey(terms));
                StageFactory.getInstance().getLastDeclaredStage().setUpdateKey(CallFactory.getInstance().getUpdateKey(terms));
            } catch (InvalidValueException | UndeclaredRegisterException | UnknownOpcodeException e) {
                compileError(e);
            }
        } else if (CallFactory.getInstance().isASetUpdateKey(functionName)) {
            try {
                // TODO: check illegal sets of updatekey
                StageFactory.getInstance().getLastDeclaredStage().setUpdateKey(CallFactory.getInstance().getUpdateKey(terms));
            } catch (InvalidValueException | UndeclaredRegisterException | UnknownOpcodeException e) {
                compileError(e);
            }
        }
        else if (CallFactory.getInstance().isAStageTransition(functionName)) {
            try {
                action = CallFactory.getInstance().getStageTransition(terms);
                CompilerStateMachine.getInstance().addAction(action);
            } catch (TooManyArgsException e) {
                compileError(e);
            }
        }
        else {
            // try to parse as a macro action call
            try {
                Vector<Action> macroAction = MacroActionsFactory.getInstance().getMacroAction(functionName);
                CompilerStateMachine.getInstance().addActions(macroAction);
            } catch (UndefinedMacroActionException e) {

                // try to parse as an HardwareAction call
                Vector<Operand> args;
                try {
                    args = OperandFactory.getInstance().getOperands(terms);
                    action = CallFactory.getInstance().getAction(functionName, args);
                    CompilerStateMachine.getInstance().addAction(action);
                } catch (UndeclaredRegisterException | InvalidValueException | TooManyArgsException e1) {
                    compileError(e1);
                } catch (UnknownOpcodeException e1) {
                    compileError(e); // it's not an hardware action, maybe a wrong macro action?
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Condition statements

    @Override
    public void enterCondition_statement(xtraParser.Condition_statementContext ctx) {
        try {
            CompilerStateMachine.getInstance().goInsideBlock();
        } catch (IllegalCompilerTransitionException e) {
            unknownError(e);
        } catch (AlreadyDefinedException e) {
            compileError(e);
        }
    }

    @Override
    public void exitCondition(xtraParser.ConditionContext ctx) {
        xtraParser.TermContext op0Ctx = ctx.term(0);
        xtraParser.TermContext op1Ctx = ctx.term(1);

        Operand op0, op1;

        ConditionOpcode opcode = null;
        if (ctx.EQ() != null)
            opcode = ConditionOpcode.XTRA_EQUAL;
        else if (ctx.NEQ() != null)
            opcode = ConditionOpcode.XTRA_NOT_EQUAL;
        else if (ctx.LEQ() != null)
            opcode = ConditionOpcode.XTRA_LESS_EQ;
        else if (ctx.GEQ() != null)
            opcode = ConditionOpcode.XTRA_GREATER_EQ;
        else if (ctx.LESS() != null)
            opcode = ConditionOpcode.XTRA_LESS;
        else if (ctx.GRET() != null)
            opcode = ConditionOpcode.XTRA_GREATER;

        assert opcode != null; // This is a problem (this shouldn't happen 'cos the parser accept only these tokens)
        try {
            op0 = OperandFactory.getInstance().getOperand(op0Ctx);
            op1 = OperandFactory.getInstance().getOperand(op1Ctx);
            Condition condition;

            if (!ConditionFactory.getInstance().isPresent(op0, op1, opcode)) {
                condition = ConditionFactory.getInstance().createCondition(op0, op1, opcode);
            } else {
                condition = ConditionFactory.getInstance().getCondition(op0, op1, opcode);
            }
            ConditionResult result = new ConditionResult(condition, Boolean.XTRA_TRUE); // TODO: optimize condition creation (# cond created)

            TableRowFactory.getInstance().addConditionResult(result);
        } catch (UndeclaredRegisterException | InvalidValueException | UnknownOpcodeException e) {
            compileError(e);
        }
    }

    @Override
    public void exitCondition_statement(xtraParser.Condition_statementContext ctx) {
        try {
            CompilerStateMachine.getInstance().goOutsideBlock();
        } catch (IllegalCompilerTransitionException e) { // This should not happen ('cos the parser accept condition statements only in event blocks)
            unknownError(e);
        } catch (UndefinedMacroActionException | UndeclaredStateException | AlreadyDefinedException e) {
            compileError(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------
    // Error handling

    private void unknownError(Exception e) {
        System.err.println("[XTRAC] unknown compile error, while entering into condition statement, printing stack trace...");
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(-1);
    }

    private void compileError(Exception e) {
        System.err.println("[XTRAC] compile error: " + e.getMessage());
        System.exit(-1);
    }
}