package it.uniroma2.netprog.xtrac.compiler.utils;

import it.uniroma2.netprog.xtrac.exception.InvalidValueException;
import it.uniroma2.netprog.xtrac.exception.UndeclaredRegisterException;
import it.uniroma2.netprog.xtrac.exception.UnknownOpcodeException;
import it.uniroma2.netprog.xtrac.model.operand.*;
import it.uniroma2.netprog.xtrac.parser.xtraParser;
import java.util.List;
import java.util.Vector;

public class OperandFactory {
    private static OperandFactory ourInstance = new OperandFactory();

    public static OperandFactory getInstance() {
        return ourInstance;
    }

    private OperandFactory() {
    }

    public Operand getOperand(xtraParser.TermContext termContext) throws InvalidValueException, UndeclaredRegisterException, UnknownOpcodeException {
       if (termContext.field_elem() != null) {
           try {
               return EventFieldFactory.getInstance().getEventField(termContext.field_elem().ID(0).getText(), termContext.field_elem().ID(1).getText());
           } catch (InvalidValueException | UnknownOpcodeException e) {
               return PacketFieldFactory.getInstance().getPacketField(termContext.field_elem().ID(0).getText()+"."+termContext.field_elem().ID(1).getText());
           }

       }
       else if (termContext.identifier() != null)
           // parse as register (local/global writable or read only)
           return getOperand(termContext.identifier());
       else if (termContext.integer() != null)
           // parse as integer (base 10 or 16)
           return getOperand(termContext.integer());
       else if (termContext.addr() != null)
           // parse as a network address (ipv4, ipv6 or mac addr)
           return getOperand(termContext.addr());
       else
           // index access
           return getOperand(termContext.index_access());
    }

    private Operand getOperand(xtraParser.IdentifierContext identifierContext) throws UndeclaredRegisterException {
        String registerName = identifierContext.getText();
        Register register;

        try {
            register = ReadOnlyRegisterFactory.getInstance().getReadOnlyRegister(registerName); // try to parse as read-only register
            return register;
        }
        catch (UndeclaredRegisterException e) {
            try {
                register = LocalRegisterFactory.getInstance().getRegister(registerName); // try to parse as local register
            }
            catch (UndeclaredRegisterException e1) {
                register = GlobalRegisterFactory.getInstance().getRegister(registerName); // try to parse as global register
            }
            return register;
        }
    }

    private Operand getOperand(xtraParser.IntegerContext integerContext) {
        if (integerContext.DECIMAL_INTEGER() != null)
            return new IntegerConstant(integerContext.getText());
        else
            return new IntegerConstant(String.valueOf(Integer.parseInt(integerContext.HEX_INTEGER().getText().substring(2), 16)));
    }

    private Operand getOperand(xtraParser.Index_accessContext index_accessContext) throws InvalidValueException, UnknownOpcodeException {
        return PacketFieldFactory.getInstance().getPacketFieldFromIndexAccess(index_accessContext.field_elem().getText(), index_accessContext.INDEX().getText());
    }

    private Operand getOperand(xtraParser.AddrContext addrContext) {
        String addr;
        if (addrContext.IPV4_ADDR() != null) {
            addr = addrContext.IPV4_ADDR().getText();
            return new IPv4(addr.substring(1, addr.length() - 1));
        }
        else if (addrContext.IPV6_ADDR() != null) {
            addr = addrContext.IPV6_ADDR().getText();
            return new IPv6(addr.substring(1, addr.length() - 1));
        }
        else {
            addr = addrContext.MAC_ADDR().getText();
            return new MAC(addr.substring(1, addr.length() - 1));
        }
    }

    public Register getRegister(String registerName) throws UndeclaredRegisterException {
        Register register;
        try {
            register = LocalRegisterFactory.getInstance().getRegister(registerName);
        } catch (UndeclaredRegisterException e) {
                register = GlobalRegisterFactory.getInstance().getRegister(registerName);
        }
        return register;
    }

    public Vector<Operand> getOperands(List<xtraParser.TermContext> terms) throws UndeclaredRegisterException, InvalidValueException, UnknownOpcodeException {
        Vector<Operand> operands = new Vector<>();

        for(xtraParser.TermContext term: terms) {
            operands.add(getOperand(term));
        }

        return operands;
    }
}