package it.uniroma2.netprog.xtrac.model.operand;

public enum OperandType {
    XTRA_GLOBAL_REGISTER,
    XTRA_LOCAL_REGISTER,
    XTRA_INTEGER_CONST,
    XTRA_IPV4_ADDR_CONST,
    XTRA_IPV6_ADDR_CONST,
    XTRA_MAC_ADDR_CONST,
    XTRA_PKT_FIELD,
    XTRA_EVENT_FIELD,
    XTRA_READ_ONLY_REGISTER;

    public static OperandType toEnum(Operand operand)
    {
        if (operand.getClass() == GlobalRegister.class)
            return XTRA_GLOBAL_REGISTER;
        else if (operand.getClass() == LocalRegister.class)
            return XTRA_LOCAL_REGISTER;
        else if (operand.getClass() == IntegerConstant.class)
            return XTRA_INTEGER_CONST;
        else if (operand.getClass() == IPv4.class)
            return XTRA_IPV4_ADDR_CONST;
        else if (operand.getClass() == IPv6.class)
            return XTRA_IPV6_ADDR_CONST;
        else if (operand.getClass() == MAC.class)
            return XTRA_MAC_ADDR_CONST;
        else if (operand.getClass() == EventField.class)
            return XTRA_EVENT_FIELD;
        else if (operand.getClass() == ReadOnlyRegister.class)
            return XTRA_READ_ONLY_REGISTER;
        else
            return XTRA_PKT_FIELD;
    }
}
