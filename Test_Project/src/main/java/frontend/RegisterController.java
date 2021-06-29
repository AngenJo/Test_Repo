package frontend;

import backend.DataMemory;
import backend.Microcontroller;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class RegisterController {

    // SPECIAL FUNCTION REGISTERS
    @FXML
    private Label wrLabel;
    @FXML
    private Label fsrLabel;
    @FXML
    private Label pclLabel;
    @FXML
    private Label pclathLabel;
    @FXML
    private Label pcLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label optionLabel;
    @FXML
    private Label intconLabel;

    // STATUS REGISTER
    @FXML
    private Label irpLabel;
    @FXML
    private Label rp1Label;
    @FXML
    private Label rp0Label;
    @FXML
    private Label toLabel;
    @FXML
    private Label pdLabel;
    @FXML
    private Label zFlagLabel;
    @FXML
    private Label dcFlagLabel;
    @FXML
    private Label cFlagLabel;

    // OPTION REGISTER
    @FXML
    private Label rbpLabel;
    @FXML
    private Label intedgLabel;
    @FXML
    private Label t0csLabel;
    @FXML
    private Label t0seLabel;
    @FXML
    private Label psaLabel;
    @FXML
    private Label ps2Label;
    @FXML
    private Label ps1Label;
    @FXML
    private Label ps0Label;

    // INTCON REGISTER
    @FXML
    private Label gieLabel;
    @FXML
    private Label pieLabel;
    @FXML
    private Label t0ieLabel;
    @FXML
    private Label inteLabel;
    @FXML
    private Label rbieLabel;
    @FXML
    private Label t0ifLabel;
    @FXML
    private Label intfLabel;
    @FXML
    private Label rbifLabel;

    @FXML
    private CheckBox checkBoxWatchdog;

    private Microcontroller mc;

    public void setMicrocontroller(Microcontroller mc) {
        this.mc = mc;
        this.checkBoxWatchdog.setSelected(true);
    }

    public void updateRegisters() {
        this.wrLabel.setText(String.format("%02X", this.mc.getWorkingRegister()));
        this.fsrLabel.setText(String.format("%02X", this.mc.getFile(DataMemory.FS_REG)));
        this.pclLabel.setText(String.format("%02X", this.mc.getFile(DataMemory.PCL_REG)));
        this.pclathLabel.setText(String.format("%02X", this.mc.getFile(DataMemory.PCLATH_REG)));
        this.pcLabel.setText(String.format("%04X", this.mc.getProgramCounter()));
        this.statusLabel.setText(String.format("%02X", this.mc.getFile(DataMemory.STATUS_REG)));
        this.optionLabel.setText(String.format("%02X", this.mc.getFile(0x80 + DataMemory.OPTION_REG)));
        this.intconLabel.setText(String.format("%02X", this.mc.getFile(DataMemory.INTCON_REG)));

        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0:
                    this.cFlagLabel.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.ps0Label.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.rbifLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 1:
                    this.dcFlagLabel.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.ps1Label.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.intfLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 2:
                    this.zFlagLabel.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.ps2Label.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.t0ifLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 3:
                    this.pdLabel.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.psaLabel.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.rbieLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 4:
                    this.toLabel.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.t0seLabel.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.inteLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 5:
                    this.rp0Label.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.t0csLabel.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.t0ieLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 6:
                    this.rp1Label.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.intedgLabel.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.pieLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                case 7:
                    this.irpLabel.setText(this.mc.getStatusRegisterBit(i) ? "1" : "0");
                    this.rbpLabel.setText(this.mc.getOptionRegisterBit(i) ? "1" : "0");
                    this.gieLabel.setText(this.mc.getInterruptRegisterBit(i) ? "1" : "0");
                    break;
                default:
                    break;
            }
        }
    }

    public void setWatchdog() {
        mc.setWatchdog(checkBoxWatchdog.isSelected());
    }
}
