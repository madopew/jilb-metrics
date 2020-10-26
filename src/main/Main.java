package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

    public class Main extends Application {
        private static Stage mainStage;

        @Override
        public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("Halstead metric.fxml"));
            primaryStage.setTitle("Halstead metric");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            mainStage = primaryStage;
        }

        public static Stage getStage() {
            return mainStage;
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
