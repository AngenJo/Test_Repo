package frontend;

import backend.Microcontroller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class IOPortController {

    @FXML
    private Label trisA4;
    @FXML
    private Label trisA3;
    @FXML
    private Label trisA2;
    @FXML
    private Label trisA1;
    @FXML
    private Label trisA0;
    @FXML
    private Label portA4;
    @FXML
    private Label portA3;
    @FXML
    private Label portA2;
    @FXML
    private Label portA1;
    @FXML
    private Label portA0;

    @FXML
    private Label trisB7;
    @FXML
    private Label trisB6;
    @FXML
    private Label trisB5;
    @FXML
    private Label trisB4;
    @FXML
    private Label trisB3;
    @FXML
    private Label trisB2;
    @FXML
    private Label trisB1;
    @FXML
    private Label trisB0;
    @FXML
    private Label portB7;
    @FXML
    private Label portB6;
    @FXML
    private Label portB5;
    @FXML
    private Label portB4;
    @FXML
    private Label portB3;
    @FXML
    private Label portB2;
    @FXML
    private Label portB1;
    @FXML
    private Label portB0;

    @FXML
    private Circle ledB7;
    @FXML
    private Circle ledB6;
    @FXML
    private Circle ledB5;
    @FXML
    private Circle ledB4;
    @FXML
    private Circle ledB3;
    @FXML
    private Circle ledB2;
    @FXML
    private Circle ledB1;
    @FXML
    private Circle ledB0;

    private Label[] portALabels;
    private Label[] trisALabels;
    private Label[] portBLabels;
    private Label[] trisBLabels;
    private Circle[] ledCircles;

    private Microcontroller mc;


    public void setMicrocontroller(Microcontroller mc) {
        this.mc = mc;
    }

    public void init() {
        this.portALabels = new Label[]{ portA0, portA1, portA2, portA3, portA4 };
        this.trisALabels = new Label[]{ trisA0, trisA1, trisA2, trisA3, trisA4 };
        this.portBLabels = new Label[]{ portB0, portB1, portB2, portB3, portB4, portB5, portB6, portB7 };
        this.trisBLabels = new Label[]{ trisB0, trisB1, trisB2, trisB3, trisB4, trisB5, trisB6, trisB7 };
        this.ledCircles = new Circle[]{ ledB0, ledB1, ledB2, ledB3, ledB4, ledB5, ledB6, ledB7 };

        for (int i = 0; i < portALabels.length; i++) {
            int finalI = i;
            this.trisALabels[i].setOnMouseClicked(event -> {
                mc.setIOTrisA(mc.getIOTrisA() ^ (1 << finalI));
                trisALabels[finalI].setText((mc.getIOTrisA() & (1 << finalI)) != 0 ? "IN" : "OUT");
            });
            this.portALabels[i].setOnMouseClicked(event -> {
                boolean trisAValue = (mc.getIOTrisA() & (1 << finalI)) != 0;
                if (trisAValue) { // Port is an input
                    mc.setIOPortA(mc.getIOPortA() ^ (1 << finalI));
                    portALabels[finalI].setText((mc.getIOPortA() & (1 << finalI)) != 0 ? "1" : "0");
                }
            });
        }
        for (int i = 0; i < portBLabels.length; i++) {
            int finalI = i;
            this.trisBLabels[i].setOnMouseClicked(event -> {
                mc.setIOTrisB(mc.getIOTrisB() ^ (1 << finalI));
                trisBLabels[finalI].setText((mc.getIOTrisB() & (1 << finalI)) != 0 ? "IN" : "OUT");
            });
            this.portBLabels[i].setOnMouseClicked(event -> {
                boolean trisBValue = (mc.getIOTrisB() & (1 << finalI)) != 0;
                if (trisBValue) { // Port is an input
                    mc.setIOPortB(mc.getIOPortB() ^ (1 << finalI));
                    portBLabels[finalI].setText((mc.getIOPortB() & (1 << finalI)) != 0 ? "1" : "0");
                }
            });
        }

        for (Circle ledCircle : ledCircles) {
            ledCircle.setFill(Paint.valueOf("BLACK"));
        }
    }

    public void updatePins() {
        for (int i = 0; i < portALabels.length; i++) {
            this.portALabels[i].setText((mc.getIOPortA() & (1 << i)) != 0 ? "1" : "0");
        }
        for (int i = 0; i < portBLabels.length; i++) {
            this.portBLabels[i].setText((mc.getIOPortB() & (1 << i)) != 0 ? "1" : "0");
        }
        for (int i = 0; i < ledCircles.length; i++) {
            this.ledCircles[i].setFill(Paint.valueOf((mc.getIOPortB() & (1 << i)) != 0 ? "RED" : "BLACK"));
        }
    }
}
