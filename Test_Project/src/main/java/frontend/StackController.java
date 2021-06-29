package frontend;

import backend.Microcontroller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class StackController {

    @FXML
    private ListView<String> stackView;

    private Microcontroller mc;

    public void setMicrocontroller(Microcontroller mc) {
        this.mc = mc;
    }

    public void updateStack() {
        int[] stack = this.mc.getStack();
        String[] addrs = new String[8];
        for (int i = 0; i < 8; i++) { addrs[i] = String.format("0x%04X", stack[i]); }
        this.stackView.setItems(FXCollections.observableArrayList(addrs));
    }

}
