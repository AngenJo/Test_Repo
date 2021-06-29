package backend;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;


public class Microcontroller implements IOpDecoder {
    private int workingRegister;
    private final Stack stack;
    private final IOPins ioPins;
    private final DataMemory dataMem;
    private final ProgMemory progMem;
    private final Watchdog watchdog;
    private int cycleCount;
    private boolean poweredDown;
    private int programCounter;
    private final Set<Integer> breakpoints;

    public Microcontroller() {
        this.workingRegister = 0;
        this.stack = new Stack();
        this.ioPins = new IOPins();
        this.watchdog = new Watchdog(this);
        this.dataMem = new DataMemory(this.ioPins, this.watchdog);
        this.progMem = new ProgMemory();
        this.programCounter = 0;
        this.breakpoints = new HashSet<>();
    }

    public void load(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.lines().forEach(line -> {
                if (!Character.isWhitespace(line.charAt(0))) {
                    String[] cmd = line.split("\\s");
                    this.progMem.setCommand(Integer.parseInt(cmd[0],16),Integer.parseInt(cmd[1],16));
                }
            });
        } catch (Exception ex) {
            // TODO handle file read exceptions
        }
        // set power-on reset conditions
        this.dataMem.write(true, DataMemory.OPTION_REG, 0xFF);
        this.dataMem.write(DataMemory.PCLATH_REG, 0x00);
        this.dataMem.write(DataMemory.INTCON_REG, 0x00);
        this.dataMem.setStatusRegisterBit(DataMemory.PDINV_BIT, true);
        this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, true);
        this.setIOTrisA(0x1F);
        this.setIOTrisB(0xFF);
    }

    // run one complete fetch-execute cycle
    public boolean runCycle() {
        // no instructions executed in power-down mode
        if (!this.poweredDown) {
            // load instruction opcode at current PC location
            int op = this.progMem.getMemory()[this.programCounter];
            // decode and execute instruction
            this.decode(op);
        }
        // count up one instruction cycle
        // instructions that take 2 instruction cycles are also counted up once in decode
        this.incrementCycleCount();
        this.checkAndHandleInterrupts();

        return this.breakpoints.contains(this.programCounter);
    }

    // add an address to the list of breakpoints
    // program execution will halt when that address is reached
    // setting a breakpoint twice will remove that breakpoint (toggle function)
    public void toggleBreakpoint(int address) {
        if (!this.breakpoints.contains(address)) {
            this.breakpoints.add(address);
        } else {
            this.breakpoints.remove(address);
        }
    }

    private void checkAndHandleInterrupts() {
        boolean t0Interrupt = this.dataMem.getInterruptRegisterBit(DataMemory.T0IE_BIT) && this.dataMem.getInterruptRegisterBit(DataMemory.T0IF_BIT);
        boolean intInterrupt = this.dataMem.getInterruptRegisterBit(DataMemory.INTE_BIT) && this.dataMem.getInterruptRegisterBit(DataMemory.INTF_BIT);
        boolean rbInterrupt = this.dataMem.getInterruptRegisterBit(DataMemory.RBIE_BIT) && this.dataMem.getInterruptRegisterBit(DataMemory.RBIF_BIT);

        if (this.dataMem.getInterruptRegisterBit(DataMemory.GIE_BIT)) {
            if (t0Interrupt || intInterrupt || rbInterrupt) {

                // if in power-down mode, wake up and start ISR
                if (this.poweredDown) {
                    this.poweredDown = false;
                }

                // clear GIE bit
                this.dataMem.setInterruptRegisterBit(DataMemory.GIE_BIT, false);
                // program counter is already set to next instruction, push onto stack
                this.stack.push(this.getProgramCounter());
                // load ISR register to PC
                this.setProgramCounter(DataMemory.ISR_FILE);
            }
        } else {
            // if in power-down and interrupted without active GIE,
            if (this.poweredDown && (t0Interrupt || intInterrupt || rbInterrupt)) {
                this.poweredDown = false;
            }
        }
    }

    public void watchdogTimeout() {
        if (this.poweredDown && (!this.dataMem.getStatusRegisterBit(DataMemory.TOINV_BIT))) {
            this.poweredDown = false;
        } else if (!this.poweredDown) {
            this.masterClearReset();
            this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, false);
            this.dataMem.setStatusRegisterBit(DataMemory.PDINV_BIT, true);
        }
    }

    public void masterClearReset() {
        this.workingRegister = 0;
        this.programCounter = 0;
        this.dataMem.write(DataMemory.PCLATH_REG, 0x00);
        this.dataMem.write(DataMemory.PCL_REG, 0x00);
        this.dataMem.setStatusRegisterBit(7, false);
        this.dataMem.setStatusRegisterBit(6, false);
        this.dataMem.setStatusRegisterBit(5, false);
        this.dataMem.setInterruptRegisterBit(7, false);
        this.dataMem.setInterruptRegisterBit(6, false);
        this.dataMem.setInterruptRegisterBit(5, false);
        this.dataMem.setInterruptRegisterBit(4, false);
        this.dataMem.setInterruptRegisterBit(3, false);
        this.dataMem.setInterruptRegisterBit(2, false);
        this.dataMem.setInterruptRegisterBit(1, false);

        if (this.poweredDown) {
            this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, true);
            this.dataMem.setStatusRegisterBit(DataMemory.PDINV_BIT, false);
            this.poweredDown = false;
        } else {
            this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, true);
            this.dataMem.setStatusRegisterBit(DataMemory.PDINV_BIT, true);
        }

    }

    /* --- IO PORT FUNCTIONS ---*/
    public void setIOPortA(int inputValue) {
        this.ioPins.setPortA(inputValue, true);
    }

    public int getIOPortA() {
        return this.ioPins.getPortA();
    }

    public void setIOPortB(int inputValue) {
        this.ioPins.setPortB(inputValue, true);
    }

    public int getIOPortB() {
        return this.ioPins.getPortB();
    }

    public void setIOTrisA(int input) {
        this.ioPins.setTrisA(input);
    }

    public int getIOTrisA() {
        return this.ioPins.getTrisA();
    }

    public void setIOTrisB(int input) {
        this.ioPins.setTrisB(input);
    }

    public int getIOTrisB() {
        return this.ioPins.getTrisB();
    }
    /* --- END IO PORT FUNCTIONS ---*/

    @Override
    public void decode(int op) {
        switch (op & 0xF000) {
            case 0x0000:
                switch (op) {
                    case IOpDecoder.OPCODE_CLRWDT:
                        // CLRWDT
                        this.dataMem.setStatusRegisterBit(DataMemory.PDINV_BIT, true);
                        this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, true);
                        this.watchdog.clear();
                        this.incrementCycleCount();
                        this.incrementProgramCounter();
                        break;
                    case IOpDecoder.OPCODE_RETFIE:
                        // RETFIE
                        this.dataMem.setInterruptRegisterBit(DataMemory.GIE_BIT, true);
                        int retfieAddr = this.stack.pop();
                        this.setProgramCounter(retfieAddr);
                        this.incrementCycleCount();
                        break;
                    case IOpDecoder.OPCODE_RETURN:
                        // RETURN
                        int retAddr = this.stack.pop();
                        this.setProgramCounter(retAddr);
                        this.incrementCycleCount();
                        break;
                    case IOpDecoder.OPCODE_SLEEP:
                        // SLEEP // go into power-down mode
                        this.dataMem.setStatusRegisterBit(DataMemory.PDINV_BIT, false);
                        this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, true);
                        this.watchdog.clear();
                        this.incrementCycleCount();
                        this.incrementProgramCounter();
                        this.poweredDown = true;
                        break;
                    default:
                        decodeByteFileOp(op);
                        break;
                }
                break;
            case 0x1000:
                decodeBitFileOp(op);
                break;
            case 0x2000:
                decodeJumpOp(op);
                break;
            case 0x3000:
                decodeLiteralOp(op);
                break;
            default:
                //error
                break;
        }
    }

    private void decodeByteFileOp(int op) {
        boolean writeback = (op & IOpDecoder.WRITEBACK_MASK) != 0;
        int fileReg = op & IOpDecoder.FILEREG_MASK;
        int fValue = this.dataMem.read(fileReg);
        int wValue = this.workingRegister;
        int result = 0;

        switch (op & 0x0F00) {
            // d = 0 w register und 1 = file
            case 0x0000:
                // MOVWF or NOP
                result = wValue;
                break;
            case 0x0100:
                // CLRF
                // CLRW
                result = 0;
                break;
            case 0x0200:
                // SUBWF
                int w2c = (- wValue) & 0x00FF;
                result = fValue + w2c;
                // C and DC registers are inverted borrow and digit borrow
                this.dataMem.setStatusRegisterBit(DataMemory.DIGIT_CARRY_BIT, !((wValue & 0x000F) > (fValue & 0x000F)));
                this.dataMem.setStatusRegisterBit(DataMemory.CARRY_BIT, !(wValue > fValue));
                break;
            case 0x0300:
                // DECF
                result = fValue - 1;
                break;
            case 0x0400:
                // IORWF
                result = wValue | fValue;
                break;
            case 0x0500:
                // ANDWF
                result = wValue & fValue;
                break;
            case 0x0600:
                // XORWF
                result = wValue ^ fValue;
                break;
            case 0x0700:
                // ADDWF
                result = wValue + fValue;
                this.dataMem.setStatusRegisterBit(DataMemory.DIGIT_CARRY_BIT, ((wValue & 0x000F) + (fValue & 0x000F)) > 0x000F);
                this.dataMem.setStatusRegisterBit(DataMemory.CARRY_BIT, (result & 0xFF00) != 0);
                break;
            case 0x0800:
                // MOVF
                result = fValue;
                break;
            case 0x0900:
                // COMF
                result = ~fValue;
                break;
            case 0x0A00:
                // INCF
                result = fValue + 1;
                break;
            case 0x0B00:
                // DECFSZ
                result = (fValue - 1) & 0xFF;
                if (result == 0) {
                    this.incrementProgramCounter();
                    this.incrementCycleCount();
                }
                break;
            case 0x0C00:
                // RRF
                result = (fValue >> 1) + ((this.dataMem.getStatusRegisterBit(DataMemory.CARRY_BIT) ? 1 : 0) << 7);
                this.dataMem.setStatusRegisterBit(DataMemory.CARRY_BIT, (fValue & 1) == 1);
                break;
            case 0x0D00:
                // RLF
                result = (fValue << 1) + (this.dataMem.getStatusRegisterBit(DataMemory.CARRY_BIT) ? 1 : 0);
                this.dataMem.setStatusRegisterBit(DataMemory.CARRY_BIT, (fValue & 0x80) == 0x80);
                break;
            case 0x0E00:
                // SWAPF
                result = ((fValue << 4) & 0xF0) + ((fValue >> 4) & 0x0F);
                break;
            case 0x0F00:
                // INCFSZ
                result = (fValue + 1) & 0xFF;
                if (result == 0) {
                    this.incrementProgramCounter();
                    this.incrementCycleCount();
                }
                break;
            default:
                // error
                break;
        }

        // MOVWF and all ops with code > 0x0A00 don't affect the zero register
        boolean zeroRegOp = !((op & 0x0F00) == 0 || (op & 0x0F00) > 0x0A00);
        this.dataMem.setStatusRegisterBit(DataMemory.ZERO_BIT, zeroRegOp && result == 0);

        if (writeback) {
            this.dataMem.write(fileReg, result & 0x00FF);
        } else {
            this.workingRegister = result & 0x00FF;
        }

        if (fileReg == DataMemory.PCL_REG) {
            this.setProgramCounter(((this.dataMem.read(DataMemory.PCLATH_REG) & 0x1F) << 8) + this.dataMem.read(DataMemory.PCL_REG));
        }

        this.incrementProgramCounter();
    }

    private void decodeBitFileOp(int op) {
        int bitSig = (op & IOpDecoder.BIT_MASK) >> 7;
        int fileReg = op & IOpDecoder.FILEREG_MASK;
        int fValue = this.dataMem.read(fileReg);
        int result = 0;
        switch (op & 0x0C00) {
            case 0x0000:
                // BCF
                result = fValue & ~(1 << bitSig);
                break;
            case 0x0400:
                // BSF
                result = fValue | (1 << bitSig);
                break;
            case 0x0800:
                // BTFSC
                if ((fValue & (1 << bitSig)) == 0) {
                    // skip next instruction (increment program counter)
                    this.incrementProgramCounter();
                    this.incrementCycleCount();
                }
                result = fValue;
                break;
            case 0x0C00:
                // BTFSS
                if ((fValue & (1 << bitSig)) != 0) {
                    // skip next instruction (increment program counter)
                    this.incrementProgramCounter();
                    this.incrementCycleCount();
                }
                result = fValue;
                break;
            default:
                // error
                break;
        }

        // write back only on BCF and BSF
        if ((op & 0x0C00) < 0x0800) {
            this.dataMem.write(fileReg, result & 0x00FF);
        }

        if (fileReg == DataMemory.PCL_REG) {
            this.setProgramCounter(((this.dataMem.read(DataMemory.PCLATH_REG) & 0x1F) << 8) + this.dataMem.read(DataMemory.PCL_REG));
        }

        this.incrementProgramCounter();
    }

    private void decodeJumpOp(int op) {
        int addr = op & IOpDecoder.ADDR_MASK;
        switch (op & 0x0800) {
            case 0x0000:
                // CALL
                this.stack.push(this.programCounter + 1);
                addr += (this.dataMem.read(DataMemory.PCLATH_REG) & 0x18) << 8;
                this.setProgramCounter(addr);
                this.incrementCycleCount();
                break;
            case 0x0800:
                // GOTO
                addr += (this.dataMem.read(DataMemory.PCLATH_REG) & 0x18) << 8;
                this.setProgramCounter(addr);
                this.incrementCycleCount();
                break;
            default:
                // error
                break;
        }

    }

    private void decodeLiteralOp(int op) {
        int wValue = this.workingRegister;
        int literal = op & IOpDecoder.LITERAL_MASK;
        int result = 0;
        switch (op & 0x0F00) {
            case 0x0000:
            case 0x0100:
            case 0x0200:
            case 0x0300:
                // MOVLW
                result = literal;
                break;
            case 0x0400:
            case 0x0500:
            case 0x0600:
            case 0x0700:
                // RETLW
                int retAddr = this.stack.pop();
                this.setProgramCounter(retAddr);
                result = literal;
                this.incrementCycleCount();
                break;
            case 0x0800:
                // IORLW
                result = wValue | literal;
                break;
            case 0x0900:
                // ANDLW
                result = wValue & literal;
                break;
            case 0x0A00:
                // XORLW
                result = wValue ^ literal;
                break;
            // op with code 0x0B00 does not exist
            case 0x0C00:
            case 0x0D00:
                // SUBLW
                int w2c = (- wValue) & 0x00FF;
                result = literal + w2c;
                // C and DC registers are inverted borrow and digit borrow
                this.dataMem.setStatusRegisterBit(DataMemory.DIGIT_CARRY_BIT, !((wValue & 0x000F) > (literal & 0x000F)));
                this.dataMem.setStatusRegisterBit(DataMemory.CARRY_BIT, !(wValue > literal));
                break;
            case 0x0E00:
            case 0x0F00:
                // ADDLW
                result = wValue + literal;
                this.dataMem.setStatusRegisterBit(DataMemory.DIGIT_CARRY_BIT, (wValue & 0x000F) + (literal & 0x000F) > 0x000F);
                this.dataMem.setStatusRegisterBit(DataMemory.CARRY_BIT, (result & 0xFF00) != 0);
                break;
            default:
                // error
                break;
        }

        // check result and set flags
        boolean zeroRegOp = (op & 0x0F00) > 0x0700;
        this.dataMem.setStatusRegisterBit(DataMemory.ZERO_BIT, zeroRegOp && result == 0);

        this.workingRegister = result & 0x00FF;

        // if op is not RETLW, increment PC
        if (!((op & 0x0F00) > 0x0300 && (op & 0x0F00) < 0x0800)) {
            this.incrementProgramCounter();
        }
    }

    private void incrementProgramCounter() {
        this.programCounter++;
        this.programCounter &= 0x1FFF;
        this.dataMem.write(DataMemory.PCL_REG, this.programCounter & 0xFF);
    }

    private void setProgramCounter(int addr) {
        this.dataMem.write(DataMemory.PCL_REG, addr & 0xFF);
        this.programCounter = addr;
    }

    private void incrementCycleCount() {
        this.cycleCount++;
        this.watchdog.incrementWatchdogTimer();

        // TMR0 not incremented in power-down mode
        if (!this.poweredDown) {
            this.dataMem.incrementTimer();
        }
    }

    /* --- GETTERS FOR TESTING ---*/
    public int getWorkingRegister() {
        return this.workingRegister;
    }

    public int getStatusRegister() {
        return this.dataMem.read(0x03);
    }

    public int getTopOfStack() {
        return this.stack.peek();
    }

    public int[] getStack() {
        return this.stack.getStack();
    }

    public int getFile(int index) {
        boolean bank = (index & 0x80) != 0;
        return this.dataMem.read(bank, index & 0x7F);
    }

    public boolean getStatusRegisterBit(int index) {
        return this.dataMem.getStatusRegisterBit(index);
    }

    public boolean getOptionRegisterBit(int index) {
        return this.dataMem.getOptionRegisterBit(index);
    }

    public boolean getInterruptRegisterBit(int index) {
        return this.dataMem.getInterruptRegisterBit(index);
    }

    public int getProgramCounter() {
        return this.programCounter;
    }

    public int getCycleCount() {
        return this.cycleCount;
    }

    public void resetCycleCount() { this.cycleCount = 0; }

    public boolean isPoweredDown() {
        return this.poweredDown;
    }
    /* --- END GETTERS FOR TESTING ---*/
    public void setWatchdog(boolean isActive) {
        this.watchdog.setActive(isActive);
    }
}
