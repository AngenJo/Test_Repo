package frontend;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class CodeLineListViewCell extends ListCell<CodeLine> {

    private PseudoClass BREAKPOINT = PseudoClass.getPseudoClass("breakpoint");

    @FXML
    private Label activeIndicator;
    @FXML
    private Label codeLineContent;
    @FXML
    private GridPane gridPane;

    private FXMLLoader loader;

    @Override
    protected void updateItem(CodeLine codeLine, boolean empty) {
        super.updateItem(codeLine, empty);

        if (empty || codeLine == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (this.loader == null) {
                this.loader = new FXMLLoader(getClass().getResource("/frontend/code_line_cell.fxml"));
                this.loader.setController(this);

                try {
                    this.loader.load();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            this.codeLineContent.setText(codeLine.getContent());
            this.pseudoClassStateChanged(BREAKPOINT, codeLine.isBreakpoint());
            if (codeLine.isBreakpoint()) {
                this.codeLineContent.setStyle("-fx-text-fill: #FFFFFF");
                this.activeIndicator.setStyle("-fx-text-fill: #FFFFFF");
            } else {
                this.codeLineContent.setStyle("-fx-text-fill: #000000");
                this.activeIndicator.setStyle("-fx-text-fill: #000000");
            }

            if (codeLine.isActive()) {
                this.activeIndicator.setText("=>");
            } else {
                this.activeIndicator.setText("  ");
            }

            setText(null);
            setGraphic(this.gridPane);
        }
    }

}
