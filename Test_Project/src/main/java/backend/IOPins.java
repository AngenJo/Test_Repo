package backend;

public class IOPins {

    private DataMemory dataMem;
    private boolean t0cki;
    private boolean intB;
    private int portA;
    private int trisA;
    private int portB;
    private int trisB;

    public void setDataMemory(DataMemory dataMem) {
        this.dataMem = dataMem;
    }

    public void setTrisA(int trisA) {
        this.trisA = trisA & 0x1F;
    }

    public void setTrisB(int trisB) {
        this.trisB = trisB & 0xFF;
    }

    public void setPortA(int portA, boolean external) {
        boolean ra4New = (portA & 0x10) != 0;
        boolean ra4Old = this.t0cki;
        this.t0cki = ra4New;

        if (ra4New ^ ra4Old) {
            this.dataMem.triggerExternalCounter(ra4New);
        }

        this.portA = external ? (this.portA & ~this.trisA) + (portA & this.trisA)
                              : (this.portA & this.trisA) + (portA & ~this.trisA);
    }

    public void setPortB(int portB, boolean external) {
        int oldPortBValue = this.portB;
        this.portB = external ? (this.portB & ~this.trisB) + (portB & this.trisB)
                              : (this.portB & this.trisB) + (portB & ~this.trisB);

        if (((this.portB & 0xF0) ^ (oldPortBValue & 0xF0)) != 0) {
            this.dataMem.setInterruptRegisterBit(DataMemory.RBIF_BIT, true);
        }

        // set INT interrupt bit
        boolean rb0New = (portB & 0x01) != 0;
        boolean rb0Old = this.intB;
        this.intB = rb0New;

        if ((rb0New ^ rb0Old) && (rb0New == this.dataMem.getOptionRegisterBit(DataMemory.INTEDG_BIT))) {
            this.dataMem.setInterruptRegisterBit(DataMemory.INTF_BIT, true);
        }
    }

    public int getPortA() {
        return this.portA & 0x1F;
    }

    public int getPortB() {
        return this.portB & 0xFF;
    }

    public int getTrisA() {
        return this.trisA & 0x1F;
    }

    public int getTrisB() {
        return this.trisB & 0xFF;
    }
}
