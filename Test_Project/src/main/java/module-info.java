module pic.simulator.main {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.common;

    opens frontend to javafx.controls, javafx.fxml;

    exports frontend;
}