package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import jilbMetrics.JilbMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    final static String ABSOLUTE = "Absolute = ";
    final static String RELATIVE = "Relative: ";
    final static String TOTAL_AMOUNT = "Total amount: ";
    final static String NESTING = "Nesting level = ";

    @FXML
    private Text absoluteString;

    @FXML
    private Text relativeString;

    @FXML
    private Text nestingString;

    @FXML
    private void exitAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void aboutDevelopers(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About developers");
        alert.setContentText("This program was developed by Bakyt Madi and Maiski Vlad, group 951007");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void aboutProgram(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About program");
        alert.setContentText("This program shows halstead metrics for a program written in Kotlin");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null && file.getAbsolutePath().matches("^.+.kt$")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String s = br.readLine();
                while (s != null) {
                    sb.append(s).append("\n");
                    s = br.readLine();
                }
            } catch (Exception e) {
                System.err.println("File error");
            }
            JilbMetrics jilbMetrics = new JilbMetrics(sb.toString());
            getAbsoluteString().setText(ABSOLUTE + jilbMetrics.getAbsoluteDifficulty());
            getNestingString().setText(NESTING + jilbMetrics.getMaxNestingLevel());
            getRelativeString().setText(RELATIVE + jilbMetrics.getRelativeDifficulty() + " (" + TOTAL_AMOUNT + jilbMetrics.getTotalOperatorAmount() + ")");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error file");
            alert.setHeaderText(null);
            alert.setContentText((file == null ? "You haven't selected a file."
                    : "Selected file is not a Kotlin program.") + " Please, try again.");
            alert.showAndWait();
        }
    }

    private Text getAbsoluteString() {
        return absoluteString;
    }

    private Text getNestingString() {
        return nestingString;
    }

    private Text getRelativeString() {
        return relativeString;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}