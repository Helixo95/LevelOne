package PokeSmart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TileWindow extends Application {

    private final int TILE_SIZE = 48; // Taille d'une tuile en pixels
    private final int NUM_TILES_X = 16; // Nombre de tuiles en largeur
    private final int NUM_TILES_Y = 12; // Nombre de tuiles en hauteur

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tile Window");

        Group root = new Group();
        Scene scene = new Scene(root, NUM_TILES_X * TILE_SIZE, NUM_TILES_Y * TILE_SIZE, Color.WHITE);

        // Chargement des données depuis le fichier CSV
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/org/example/codev2/Tiles/world1.csv"))) {
            String line;
            int rowIndex = 0;
            while ((line = reader.readLine()) != null && rowIndex < NUM_TILES_Y) {
                String[] values = line.split(",");
                int columnIndex = 0;
                for (String value : values) {
                    int intValue = Integer.parseInt(value.trim());
                    Image image = null;
                    switch (intValue) {
                        case 0:
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/codev2/Tiles/ground/wall.png")));
                            break;
                        case 1:
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/codev2/Tiles/ground/water.png")));
                            break;
                        case 2:
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/codev2/Tiles/ground/sand.png")));
                            break;
                        case 3:
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/codev2/Tiles/ground/earth.png")));
                            break;
                        case 4:
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/codev2/Tiles/ground/grass.png")));
                            break;
                    }
                    if (image != null) {
                        ImageView imageView = new ImageView(image);
                        imageView.setX(columnIndex * TILE_SIZE);
                        imageView.setY(rowIndex * TILE_SIZE);
                        imageView.setFitWidth(TILE_SIZE);
                        imageView.setFitHeight(TILE_SIZE);
                        root.getChildren().add(imageView);
                    }
                    columnIndex++;
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

