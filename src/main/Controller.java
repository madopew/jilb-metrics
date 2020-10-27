package main;

import halsteadMetrics.Argument;
import halsteadMetrics.HalsteadMetricsCondensed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import jilbMetrics.JilbMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    final static String CL_ABSOLUTE_STRING = "CL = ";
    final static String CL_RELATIVE_STRING = "cl = ";
    final static String CLI_STRING = "CLI = ";

    @FXML
    private Text CL;

    @FXML
    private Text cl;

    @FXML
    private Text CLI;

    @FXML
    private Text n1Value;

    @FXML
    private TableView<Item> tableOperator;

    @FXML
    private TableColumn<Item, Integer> j;

    @FXML
    private TableColumn<Item, String> operator;

    @FXML
    private TableColumn<Item, Integer> f1j;

    private static ObservableList<Item> dataOperator = FXCollections.observableArrayList();

    private static ObservableList<Item> dataOperand  = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        j.setCellValueFactory(new PropertyValueFactory<>("id"));
        operator.setCellValueFactory(new PropertyValueFactory<>("item"));
        f1j.setCellValueFactory(new PropertyValueFactory<>("counter"));
    }

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
        alert.setContentText("This program shows Jilb metrics for a program written in Kotlin");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null && file.getAbsolutePath().matches("^.+.kt$")) {
            dataOperand.clear();
            dataOperator.clear();
            StringBuilder sb = new StringBuilder("");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String s = br.readLine();
                while(s != null) {
                    sb.append(s).append("\n");
                    s = br.readLine();
                }
            } catch (Exception e) {
                System.err.println("File error");
            }
            HalsteadMetricsCondensed halsteadMetricsCondensed = new HalsteadMetricsCondensed(sb.toString());
            JilbMetrics jilbMetrics = new JilbMetrics(halsteadMetricsCondensed);
            getN1().setText(Integer.toString(halsteadMetricsCondensed.getOperatorsAmount()));
            getCLAbsolute().setText(CL_ABSOLUTE_STRING + jilbMetrics.getAbsoluteDifficulty());
            getCLRelative().setText(CL_RELATIVE_STRING + String.format("%.3f", jilbMetrics.getRelativeDifficulty()));
            getCLI().setText(CLI_STRING + jilbMetrics.getMaxNestingLevel());
            fillData(halsteadMetricsCondensed.getOperators());
            fillTables();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error file");
            alert.setHeaderText(null);
            alert.setContentText((file == null ? "You haven't selected a file."
                    : "Selected file is not a Kotlin program.") + " Please, try again.");
            alert.showAndWait();
        }
    }

    private static void fillData(ArrayList<Argument> operators) {
        int id = 1;
        for (Argument op: operators) {
            dataOperator.add(new Item(op.value, op.amount, id++));
        }
    }

    public Text getN1() {
        return n1Value;
    }

    public Text getCLAbsolute() {
        return CL;
    }

    public Text getCLRelative() {
        return cl;
    }

    public Text getCLI() {
        return CLI;
    }

    public void fillTables() {
        tableOperator.setItems(dataOperator);
    }
}