import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.Microcontroller;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MicrocontrollerTest {

    private Microcontroller mc;

    @BeforeEach
    void initMicrocontroller() {
        this.mc = new Microcontroller();
    }

    // TPicSim1
    @Test
    void basicLiteralOpTest() {
        File input = new File("src/test/resources/TPicSim1.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());
        this.mc.runCycle(); // movlw 11h
        assertEquals(0x11, this.mc.getWorkingRegister());
        this.mc.runCycle(); // andlw 30h
        assertEquals(0x10, this.mc.getWorkingRegister());
        assertEquals(0, this.mc.getStatusRegister() & 0b100);
        this.mc.runCycle(); // iorlw 0Dh
        assertEquals(0x1D, this.mc.getWorkingRegister());
        assertEquals(0, this.mc.getStatusRegister() & 0b100);
        this.mc.runCycle(); // sublw 3Dh
        assertEquals(0x20, this.mc.getWorkingRegister());
        assertEquals(0b011, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // xorlw 20h
        assertEquals(0x00, this.mc.getWorkingRegister());
        assertEquals(0b111, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // addlw 25h
        assertEquals(0x25, this.mc.getWorkingRegister());
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
    }

    // TPicSim2
    @Test
    void basicJumpOpTest() {
        File input = new File("src/test/resources/TPicSim2.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());
        this.mc.runCycle(); // movlw 11h
        assertEquals(0x11, this.mc.getWorkingRegister());
        this.mc.runCycle(); // call up1
        assertEquals(0x06, this.mc.getProgramCounter());
        assertEquals(0x02, this.mc.getTopOfStack());
        this.mc.runCycle(); // addlw 25h
        assertEquals(0x36, this.mc.getWorkingRegister());
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // return
        assertEquals(0x02, this.mc.getProgramCounter());
        this.mc.runCycle(); // nop
        assertEquals(0x36, this.mc.getWorkingRegister());
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // call up2
        assertEquals(0x08, this.mc.getProgramCounter());
        assertEquals(0x04, this.mc.getTopOfStack());
        this.mc.runCycle(); // retlw 77h
        assertEquals(0x04, this.mc.getProgramCounter());
        assertEquals(0x77, this.mc.getWorkingRegister());
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
    }

    // TPicSim3
    @Test
    void basicFileOpTest() {
        final int VAL1 = 0x0C;
        final int VAL2 = 0x0D;
        final int RES  = 0x0E;

        File input = new File("src/test/resources/TPicSim3.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());
        this.mc.runCycle(); // movlw 11h
        assertEquals(0x11, this.mc.getWorkingRegister());
        this.mc.runCycle(); // movwf val1
        assertEquals(0x11, this.mc.getFile(VAL1));
        this.mc.runCycle(); // movlw 14h
        assertEquals(0x14, this.mc.getWorkingRegister());
        this.mc.runCycle(); // addwf val1,w
        assertEquals(0x25, this.mc.getWorkingRegister());
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // addwf val1
        assertEquals(0x25, this.mc.getWorkingRegister());
        assertEquals(0x36, this.mc.getFile(VAL1));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // andwf val1,w
        assertEquals(0x24, this.mc.getWorkingRegister());
        assertEquals(0x36, this.mc.getFile(VAL1));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // movwf val2
        assertEquals(0x24, this.mc.getWorkingRegister());
        assertEquals(0x36, this.mc.getFile(VAL1));
        assertEquals(0x24, this.mc.getFile(VAL2));
        this.mc.runCycle(); // clrf val1
        assertEquals(0x24, this.mc.getWorkingRegister());
        assertEquals(0x00, this.mc.getFile(VAL1));
        assertEquals(0x24, this.mc.getFile(VAL2));
        assertEquals(0b100, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // comf val2,w
        assertEquals(0xDB, this.mc.getWorkingRegister());
        assertEquals(0x00, this.mc.getFile(VAL1));
        assertEquals(0x24, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // decf val1,w
        assertEquals(0xFF, this.mc.getWorkingRegister());
        assertEquals(0x00, this.mc.getFile(VAL1));
        assertEquals(0x24, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // incf val2
        assertEquals(0xFF, this.mc.getWorkingRegister());
        assertEquals(0x00, this.mc.getFile(VAL1));
        assertEquals(0x25, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // movf val1
        assertEquals(0xFF, this.mc.getWorkingRegister());
        assertEquals(0x00, this.mc.getFile(VAL1));
        assertEquals(0x25, this.mc.getFile(VAL2));
        assertEquals(0b100, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // iorwf val1
        assertEquals(0xFF, this.mc.getWorkingRegister());
        assertEquals(0xFF, this.mc.getFile(VAL1));
        assertEquals(0x25, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // subwf val2,w
        assertEquals(0x26, this.mc.getWorkingRegister());
        assertEquals(0xFF, this.mc.getFile(VAL1));
        assertEquals(0x25, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // swapf val2
        assertEquals(0x26, this.mc.getWorkingRegister());
        assertEquals(0xFF, this.mc.getFile(VAL1));
        assertEquals(0x52, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // xorwf val1
        assertEquals(0x26, this.mc.getWorkingRegister());
        assertEquals(0xD9, this.mc.getFile(VAL1));
        assertEquals(0x52, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // clrw
        assertEquals(0x00, this.mc.getWorkingRegister());
        assertEquals(0xD9, this.mc.getFile(VAL1));
        assertEquals(0x52, this.mc.getFile(VAL2));
        assertEquals(0b100, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // subwf val1,w
        assertEquals(0xD9, this.mc.getWorkingRegister());
        assertEquals(0xD9, this.mc.getFile(VAL1));
        assertEquals(0x52, this.mc.getFile(VAL2));
        assertEquals(0b011, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // subwf val2,w
        assertEquals(0x79, this.mc.getWorkingRegister());
        assertEquals(0xD9, this.mc.getFile(VAL1));
        assertEquals(0x52, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // subwf val2
        assertEquals(0x79, this.mc.getWorkingRegister());
        assertEquals(0xD9, this.mc.getFile(VAL1));
        assertEquals(0xD9, this.mc.getFile(VAL2));
        assertEquals(0b000, this.mc.getStatusRegister() & 0b111);
        this.mc.runCycle(); // subwf val2
        assertEquals(0x79, this.mc.getWorkingRegister());
        assertEquals(0xD9, this.mc.getFile(VAL1));
        assertEquals(0x60, this.mc.getFile(VAL2));
        assertEquals(0b011, this.mc.getStatusRegister() & 0b111);
    }

    // TPicSim4
    @Test
    void skipIfZeroTest() {
        File input = new File("src/test/resources/TPicSim4.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x1C) {
            this.mc.runCycle();
        }
    }

    // TPicSim5
    @Test
    void bitLevelOpsTest() {
        final int VAL2 = 0x0D;

        File input = new File("src/test/resources/TPicSim5.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x13) {
            this.mc.runCycle();
        }

        assertEquals(0x04, this.mc.getFile(VAL2));
    }

    // TPicSim6
    @Test
    void indirectAddressingTest() {
        File input = new File("src/test/resources/TPicSim6.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x27) {
            this.mc.runCycle();
        }
    }

    // TPicSim7
    @Test
    void timerTest() {
        File input = new File("src/test/resources/TPicSim7.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x19) {
            this.mc.runCycle();
        }

        assertEquals(0x31, this.mc.getFile(0x10));

        while (this.mc.getProgramCounter() != 0x20) {
            this.mc.setIOPortA(0x10);
            this.mc.runCycle();
            if (this.mc.getProgramCounter() == 0x20) { break; }
            this.mc.setIOPortA(0x00);
            this.mc.runCycle();
        }

        assertEquals(0x10, this.mc.getFile(0x01));

        while (this.mc.getProgramCounter() != 0x27) {
            this.mc.setIOPortA(0x10);
            this.mc.runCycle();
            if (this.mc.getProgramCounter() == 0x27) { break; }
            this.mc.setIOPortA(0x00);
            this.mc.runCycle();
        }

        assertEquals(0x08, this.mc.getFile(0x01));
    }

    // TPicSim8
    @Test
    void interruptTest() {
        File input = new File("src/test/resources/TPicSim8.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x2C) {
            this.mc.runCycle();
        }


        this.mc.runCycle();
        this.mc.setIOPortB(0x00);
        this.mc.runCycle();
        this.mc.setIOPortB(0x01);
        this.mc.runCycle();
        this.mc.setIOPortB(0x00);

        while (this.mc.getProgramCounter() != 0x1B) {
            this.mc.runCycle();
        }

        this.mc.runCycle();
        this.mc.setIOTrisB(0xF0);
        this.mc.setIOPortB(0xF0);
        this.mc.runCycle();

        while (this.mc.getProgramCounter() != 0x3C) {
            this.mc.setIOPortB(0xF0);
            this.mc.runCycle();
            if (this.mc.getProgramCounter() == 0x3C) { break; }
            this.mc.setIOPortB(0x00);
            this.mc.runCycle();
        }

        assertEquals(0x54, this.mc.getFile(0x20));
        assertEquals(0x49, this.mc.getFile(0x21));
        assertEquals(0x52, this.mc.getFile(0x22));
    }

    // TPicSim9
    @Test
    void sleepTest() {
        File input = new File("src/test/resources/TPicSim9.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x06) {
            this.mc.runCycle();
        }

        assertTrue(this.mc.getCycleCount() > 18000); // ran more cycles than WDT timeout cycles
    }

    // TPicSim10
    @Test
    void pclathTest() {
        File input = new File("src/test/resources/TPicSim10.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        while (this.mc.getProgramCounter() != 0x11) {
            this.mc.runCycle();
        }
    }

    // TPicSim11
    @Test
    void watchdogTest() {
        File input = new File("src/test/resources/TPicSim11.LST");
        assertTrue(input.exists());
        this.mc.load(input.getPath());

        this.mc.runCycle();
        while (this.mc.getProgramCounter() != 0x00) {
            this.mc.runCycle();
        }
    }
}