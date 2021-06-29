package backend;


public class Watchdog {
    private static final int WDT_LIMIT = 18000;

    private Microcontroller mc;
    private DataMemory dataMem;
    private int watchdogTimer;
    private int postscalerAcc;
    private int postscalerRatio;
    private boolean isActive;

    public Watchdog(Microcontroller mc) {
        this.mc = mc;
        this.watchdogTimer = 0;
        this.postscalerRatio = 1;
        this.isActive = true;
    }

    public void setDataMemory(DataMemory dataMem) {
        this.dataMem = dataMem;
    }

    public void clear() {
        this.watchdogTimer = 0;
        if (this.dataMem.getOptionRegisterBit(DataMemory.PSA_BIT)) {
            this.postscalerAcc = 0;
        }
    }

    public void incrementWatchdogTimer() {
        if (isActive) {
            this.watchdogTimer++;
        }
        if (this.watchdogTimer > WDT_LIMIT) {
            if (this.dataMem.getOptionRegisterBit(DataMemory.PSA_BIT)) {
                this.postscalerAcc++;
                if (this.postscalerAcc == this.postscalerRatio) {
                    this.timeout();
                    this.postscalerAcc = 0;
                }
            } else {
                this.timeout();
            }
            this.watchdogTimer = 0;
        }
    }

    private void timeout() {
        this.dataMem.setStatusRegisterBit(DataMemory.TOINV_BIT, false);
        this.mc.watchdogTimeout();
    }

    public void setPostscalerRatio(int bits) {
        this.postscalerRatio = 1 << bits;
    }
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
}
