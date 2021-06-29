package frontend;

import backend.Microcontroller;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulatorMainController {

    PseudoClass breakpointMarker = PseudoClass.getPseudoClass("breakpoint");

    @FXML
    private MemoryController memoryController;
    @FXML
    private RegisterController registerController;
    @FXML
    private IOPortController ioPortController;
    @FXML
    private StackController stackController;

    @FXML
    private Button loadButton;

    @FXML
    private ListView<CodeLine> codeView;
    @FXML
    private Button stepButton;
    @FXML
    private Button runButton;
    @FXML
    private Button ffwdToBreakpointButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label runtimeLabel;

    private Microcontroller mc;


    private ObservableList<CodeLine> codeLines;
    private BiMap<Integer, Integer> cmdToLine;
    private Timeline progRunner;
    private boolean running;
    private String listingFilePath;

    @FXML
    private void initialize() {
        this.stepButton.setDisable(true);
        this.runButton.setDisable(true);
        this.resetButton.setDisable(true);
        this.ffwdToBreakpointButton.setDisable(true);

        this.memoryController.setMicrocontroller(this.mc);
        this.registerController.setMicrocontroller(this.mc);
        this.ioPortController.setMicrocontroller(this.mc);
        this.stackController.setMicrocontroller(this.mc);
        this.memoryController.init();
        this.ioPortController.init();
    }

    public void loadListing() {
        this.mc = new Microcontroller();
        this.initialize();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a PIC Code Listing");
        File listing = fc.showOpenDialog(this.loadButton.getScene().getWindow());
        this.listingFilePath = listing.getAbsolutePath();

        // load code into code view
        this.loadCodeView(this.listingFilePath);
        // load code into microcontroller
        this.mc.load(this.listingFilePath);

        this.progRunner = new Timeline(new KeyFrame(Duration.seconds(0.25), event -> {
            boolean breakpointReached = this.mc.runCycle();
            this.update();
            if (breakpointReached) {
                this.progRunner.pause();
                this.progRunner.setRate(1.0);
            }
        }));
        this.progRunner.setCycleCount(Timeline.INDEFINITE);

        this.stepButton.setDisable(false);
        this.runButton.setDisable(false);
        this.resetButton.setDisable(false);
        this.ffwdToBreakpointButton.setDisable(true);
    }

    public void doRun() {
        if (!this.running) {
            this.running = true;
            this.progRunner.play();
            this.runButton.setText("Stop");
            this.ffwdToBreakpointButton.setDisable(false);
        } else {
            this.running = false;
            this.progRunner.pause();
            this.runButton.setText("Run");
            this.ffwdToBreakpointButton.setDisable(true);
        }
    }

    public void doFFWDToBreakpoint() {
        if (this.running) {
            this.progRunner.setRate(100.0);
        }
    }

    public void doStep() {
        // if program is automatically running, pause it
        if (this.running) {
            this.doRun();
        }
        this.mc.runCycle();
        this.update();
    }

    public void doReset() {
        if (this.running) {
            this.running = false;
        }
        this.mc.masterClearReset();
        this.update();
    }

    private void update() {
        this.memoryController.updateMemory();
        this.registerController.updateRegisters();
        this.stackController.updateStack();
        this.ioPortController.updatePins();
        this.updateCodeView();
        this.updateRuntime();
    }

    public void loadCodeView(String listingPath) {
        this.cmdToLine = HashBiMap.create();
        List<CodeLine> progListing = new ArrayList<>();
        List<Integer> tempCmdToLine = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(listingPath))) {
            AtomicInteger idx = new AtomicInteger();
            br.lines().forEachOrdered(line -> {
                progListing.add(new CodeLine(line));
                if (!Character.isWhitespace(line.charAt(0))) {
                    tempCmdToLine.add(idx.getAndIncrement());
                } else {
                    idx.getAndIncrement();
                }
            });
        } catch (Exception ex) {
            // TODO handle file read exceptions
        }

        for (int i = 0; i < tempCmdToLine.size(); i++) {
            this.cmdToLine.put(i, tempCmdToLine.get(i));
        }

        this.codeLines = FXCollections.observableArrayList(progListing);
        this.codeView.setItems(this.codeLines);
        this.codeView.setCellFactory(codeLineView -> new CodeLineListViewCell());
        this.codeView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                if (cmdToLine.containsValue(codeView.getSelectionModel().getSelectedIndex())) {
                    mc.toggleBreakpoint(cmdToLine.inverse().get(codeView.getSelectionModel().getSelectedIndex()));
                    codeView.getSelectionModel().getSelectedItem().setBreakpoint(!codeView.getSelectionModel().getSelectedItem().isBreakpoint());
                }
                codeView.refresh();
            }
        });
    }

    public void updateCodeView() {
        int activeLine = this.cmdToLine.get(this.mc.getProgramCounter());
        this.codeView.getSelectionModel().select(activeLine);
        this.codeView.getItems().forEach(cl -> { cl.setActive(false); });
        this.codeView.getSelectionModel().getSelectedItem().setActive(true);
        this.codeView.getFocusModel().focus(activeLine);
        this.codeView.scrollTo(activeLine);
    }

    public void updateRuntime() {
        this.runtimeLabel.setText(this.mc.getCycleCount() + ".0");
    }

}
