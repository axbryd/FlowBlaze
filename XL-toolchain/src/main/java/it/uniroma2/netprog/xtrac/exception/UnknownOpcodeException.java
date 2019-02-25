package it.uniroma2.netprog.xtrac.exception;

public class UnknownOpcodeException extends ParserException{
    public UnknownOpcodeException(String message) {
        super(message);
    }
}
