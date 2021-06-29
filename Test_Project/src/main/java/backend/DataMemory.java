package backend;

public class DataMemory {
    public static final int TMR0_REG = 0x01;

    public static final int OPTION_REG = 0x01;
    public static final int PSA_BIT = 3;
    public static final int T0SE_BIT = 4;
    public static final int T0CS_BIT = 5;
    public static final int INTEDG_BIT = 6;

    public static final int PCL_REG = 0x02;

    public static final int STATUS_REG = 0x03;
    public static final int CARRY_BIT = 0;
    public static final int DIGIT_CARRY_BIT = 1;
    public static final int ZERO_BIT = 2;
    public static final int PDINV_BIT = 3;
    public static final int TOINV_BIT = 4;
    public static final int RP0_BIT = 5;

    public static final int FS_REG = 0x04;

    public static final int PORTA_REG = 0x05;
    public static final int TRISA_REG = 0x05;
    public static final int PORTB_REG = 0x06;
    public static final int TRISB_REG = 0x06;

    public static final int PCLATH_REG = 0x0A;

    public static final int INTCON_REG = 0x0B;
    public static final int RBIF_BIT = 0;
    public static final int INTF_BIT = 1;
    public static final int T0IF_BIT = 2;
    public static final int RBIE_BIT = 3;
    public static final int INTE_BIT = 4;
    public static final int T0IE_BIT = 5;
    public static final int EEIE_BIT = 6;
    public static final int GIE_BIT = 7;

    public static final int ISR_FILE = 0x04;

    private final int[] bank0;
    private final int[] bank1;
    private final TimerCounter timer;
    private final Watchdog watchdog;
    private final IOPins ioPins;

    public DataMemory(IOPins ioPins, Watchdog watchdog) {
        this.bank0 = new int[128];
        this.bank1 = new int[128];
        this.timer = new TimerCounter(this);
        this.watchdog = watchdog;
        this.ioPins = ioPins;
        this.ioPins.setDataMemory(this);
        this.watchdog.setDataMemory(this);
    }

    public int read(int index) {
        if (index == 0x00) {
            return this.read((this.getFSR() & 0x80) != 0, this.getFSR() & 0x7F);
        } else {
            return this.read(this.getStatusRegisterBit(RP0_BIT), index);
        }
    }

    public int read(boolean bank, int index) {
        if (!bank) {
            switch (index) {
                case TMR0_REG:
                    return this.timer.getTimer0();
                case PORTA_REG:
                    return this.ioPins.getPortA();
                case PORTB_REG:
                    return this.ioPins.getPortB();
                default:
                    return bank0[index];
            }
        } else {
            switch (index) {
                case TRISA_REG:
                    return this.ioPins.getTrisA();
                case TRISB_REG:
                    return this.ioPins.getTrisB();
                default:
                    return bank1[index];
            }
        }
    }

    public void write(int index, int value) {
        if (index == 0x00) {
            this.write((this.getFSR() & 0x80) != 0, this.getFSR() & 0x7F, value);
        } else {
            this.write(this.getStatusRegisterBit(RP0_BIT), index, value);
        }
    }

    public void write(boolean bank, int index, int value) {
        switch (index) {
            case PCL_REG:
            case FS_REG:
            case PCLATH_REG:
            case INTCON_REG:
                bank0[index] = value;
                bank1[index] = value;
                break;
            case STATUS_REG:
                // TO and PD bits are read-only (except when SLEEP or CLRWDT is executed)
                bank0[index] = (bank0[index] & 0x18) + (value & (~0x18));
                bank1[index] = (bank1[index] & 0x18) + (value & (~0x18));
                break;
            default:
                break;
        }

        if (!bank) {
            switch (index) {
                case TMR0_REG:
                    this.timer.setTimer0(value);
                    this.timer.resetPrescaler();
                    break;
                case PORTA_REG:
                    this.ioPins.setPortA(value, false);
                    break;
                case PORTB_REG:
                    this.ioPins.setPortB(value, false);
                    break;
                default:
                    break;
            }
            bank0[index] = value;
        } else {
            switch (index) {
                case OPTION_REG:
                    this.timer.setPrescalerRatio(value & 0x07);
                    this.watchdog.setPostscalerRatio(value & 0x07);
                    break;
                case TRISA_REG:
                    this.ioPins.setTrisA(value);
                    break;
                case TRISB_REG:
                    this.ioPins.setTrisB(value);
                    break;
                default:
                    break;
            }
            bank1[index] = value;
        }
    }

    private boolean getRegisterBit(boolean bank, int registerIndex, int bitIndex) {
        return bank ? (bank1[registerIndex] & (1 << bitIndex)) != 0 : (bank0[registerIndex] & (1 << bitIndex)) != 0;
    }

    private void setRegisterBit(boolean bank, int registerIndex, int bitIndex, boolean value) {
        if (value) {
            if (bank) {
                bank1[registerIndex] |= 1 << bitIndex;
            } else {
                bank0[registerIndex] |= 1 << bitIndex;
            }
        } else {
            if (bank) {
                bank1[registerIndex] &= ~(1 << bitIndex);
            } else {
                bank0[registerIndex] &= ~(1 << bitIndex);
            }
        }
    }

    /* --- REGISTER BIT FUNCTIONS --- */
    public boolean getStatusRegisterBit(int bitIndex) {
        return this.getRegisterBit(false, STATUS_REG, bitIndex);
    }

    public void setStatusRegisterBit(int bitIndex, boolean value) {
        this.setRegisterBit(false, STATUS_REG, bitIndex, value);
        this.setRegisterBit(true,  STATUS_REG, bitIndex, value);
    }

    public boolean getInterruptRegisterBit(int bitIndex) {
        return this.getRegisterBit(false, INTCON_REG, bitIndex);
    }

    public void setInterruptRegisterBit(int bitIndex, boolean value) {
        this.setRegisterBit(false, INTCON_REG, bitIndex, value);
        this.setRegisterBit(true,  INTCON_REG, bitIndex, value);
    }

    public boolean getOptionRegisterBit(int bitIndex) {
        return this.getRegisterBit(true, OPTION_REG, bitIndex);
    }
    /* --- END REGISTER BIT FUNCTIONS --- */

    // FILE ADDRESS REGISTER
    public int getFSR() {
        return bank0[FS_REG];
    }

    /* --- TIMER0 MODULE FUNCTIONS --- */
    public void incrementTimer() {
        this.timer.incrementTimer();
        this.watchdog.incrementWatchdogTimer();
    }

    public void triggerExternalCounter(boolean isRising) {
        this.timer.triggerExternalCounter(isRising);
    }
    /* --- END TIMER0 MODULE FUNCTIONS --- */
}
