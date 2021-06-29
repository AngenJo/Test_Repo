package backend;

public class TimerCounter {
    private DataMemory dataMem;
    private int timer0;
    private int prescalerAcc;
    private int prescalerRatio;
    private int syncCycles;

    public TimerCounter(DataMemory dataMem) {
        this.dataMem = dataMem;
        this.syncCycles = 0;
        this.prescalerAcc = 0;
        this.setPrescalerRatio(0);
    }

    public void setTimer0(int timer0) {
        this.syncCycles = 2;
        this.timer0 = timer0;
    }

    public int getTimer0() {
        return this.timer0;
    }

    public void incrementTimer() {
        if (!this.dataMem.getOptionRegisterBit(DataMemory.T0CS_BIT)) {
            if (!this.dataMem.getOptionRegisterBit(DataMemory.PSA_BIT)) {
                this.prescalerAcc++;
                if (this.prescalerAcc == this.prescalerRatio) {
                    this.incrementTMR0Register();
                    this.prescalerAcc = 0;
                }
            } else {
                this.incrementTMR0Register();
            }
        }
    }

    public void triggerExternalCounter(boolean isRising) {
        // handle external input, basically same as above
        if (this.dataMem.getOptionRegisterBit(DataMemory.T0CS_BIT)) {
            if (isRising ^ this.dataMem.getOptionRegisterBit(DataMemory.T0SE_BIT)) {
                if (!this.dataMem.getOptionRegisterBit(DataMemory.PSA_BIT)) {
                    this.prescalerAcc++;
                    if (this.prescalerAcc == this.prescalerRatio) {
                        this.incrementTMR0Register();
                        this.prescalerAcc = 0;
                    }
                } else {
                    this.incrementTMR0Register();
                }
            }
        }
    }

   private void incrementTMR0Register() {
       if (this.syncCycles > 0) {
           this.syncCycles--;
       }
       // TMR0 register counted up depending on sync cycle status
       this.timer0++;
       if (this.timer0 > 0xFF) {
           this.dataMem.setInterruptRegisterBit(DataMemory.T0IF_BIT, true);
           this.timer0 = 0;
       }
   }

   public void setPrescalerRatio(int bits) {
       this.prescalerRatio = 1 << (bits + 1);
   }

   public void resetPrescaler() {
        this.prescalerAcc = 0;
   }

}
