package backend;

public interface IOpDecoder {
    int OPCODE_CLRWDT = 0x0064;
    int OPCODE_RETFIE = 0x0009;
    int OPCODE_RETURN = 0x0008;
    int OPCODE_SLEEP  = 0x0063;
    int WRITEBACK_MASK = 0x0080;
    int FILEREG_MASK = 0x007F;
    int BIT_MASK = 0x0380;
    int LITERAL_MASK = 0x00FF;
    int ADDR_MASK = 0x07FF;

    void decode(int op);

}
