package PokeSmart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;

public class TestInterface extends Application {
    @Override
    public void start(Stage stage) throws IOException { //stage = fenêtre
        int j = 4;
        if (j == 2) {
            stage.setTitle("Button2");
            Button button = new Button();
            Scene scene = new Scene(button);
            scene.getStylesheets().add("org/example/codev2/Items/style.css");
            button.setPrefSize(200, 200);
            button.getStylesheets().add("org/example/codev2/Items/style.css");
            button.setId("button");
            stage.setScene(scene);
        }
        if (j == 3) {
            stage.setTitle("BorderPane");
            BorderPane pane = new BorderPane();
            pane.setBottom(new Button("Bottom"));
            pane.setTop(new Button("Top"));
            pane.setLeft(new Button("Left"));
            pane.setRight(new Button("Right"));
            pane.setCenter(new Button("Center"));
            Scene scene = new Scene(pane);
            stage.setScene(scene);
        }
        if (j == 4) {
            stage.setTitle("TilePane");
            TilePane pane = new TilePane();
            pane.setHgap(10);
            pane.setVgap(10);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}