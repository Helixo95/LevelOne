package PokeSmart;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestTileGrid extends Application {
    private final int TILE_SIZE = 48; // Taille d'une tuile en pixels
    private final int NUM_TILES_X = 16; // Nombre de tuiles en largeur
    private final int NUM_TILES_Y = 12; // Nombre de tuiles en hauteur

    private Player player;
    private HealPotion healPotion;
    private WallPotion wallPotion;
    private Monster monster;
    private Key key;
    private NPC npc;
    private boolean[][] collisionMap;
    private List<Item> items;

    private VBox inventoryBox;

    private void initWorld(){
        player = new Player("Popo", 6, 1, 0, 0, 100, 100, 2, 100, 1000, 3);
        monster = new Monster("Papa", 7, 2, 0, 0, 1, "OFFENSIVE", 1, 1, 1, 1);
        npc = new NPC("Jojo", 7,8,0,0,1,3);

        items = new ArrayList<Item>();
        items.add(new Item(7,3,"HealPotion", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));
        items.add(new Item(7,4,"WallPotion", "no more walls", Effet.OVERWALL,1,"src/PokeSmart/Object/potion_blue.png"));
        items.add(new Item(7,5,"Key", "this can open doors", Effet.OPENDOOR,1,"src/PokeSmart/Object/key.png"));


        /*
        items.add(key = new Key(7,5));
        items.add(healPotion = new HealPotion(7,3));
        items.add(wallPotion = new WallPotion(7,4));*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PokeSmart");
        String worldPath = "src/PokeSmart/Tiles/world1.csv";
        GenMap(primaryStage, worldPath);

    }

    private void GenMap(Stage primaryStage, String imagePath){
        // Chargement des données depuis le fichier CSV
        Image[][] tileImages = loadTileImages(imagePath);

        // Création de la carte
        GridPane gridPane = createMap(tileImages);

        // Création des éléments supplémentaires dans la VBox
        VBox vBox = new VBox();
        // Ajoutez vos éléments supplémentaires ici, par exemple :
        // vBox.getChildren().add(new Button("Button"));
        // Create inventory VBox
        inventoryBox = new VBox();
        //updateInventoryBox();

        // Création de la scène
        Group root = new Group();
        Scene scene = new Scene(root, NUM_TILES_X * TILE_SIZE + 200, NUM_TILES_Y * TILE_SIZE);
        root.getChildren().addAll(gridPane, vBox);

        initWorld();

        ImageView playerImageView = new ImageView(player.getPlayerImage());
        showEntities(playerImageView, root);


        scene.setOnKeyPressed(e -> {
            double x = player.getX();
            double y = player.getY();
            switch (e.getCode()) {
                case UP:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_up_1.png"));
                    if (y > 0 && !collisionMap[(int) x][(int) y - 1]) {
                        y--;
                        player.setY(y);
                    }
                    break;
                case DOWN:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_down_1.png"));
                    if (y < NUM_TILES_Y - 1 && !collisionMap[(int) x][(int) y + 1]) {
                        y++;
                        player.setY(y);
                    }
                    break;
                case LEFT:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_left_1.png"));
                    if(x > 0 && !collisionMap[(int) x - 1][(int) y]) {
                        x--;
                        player.setX(x);
                    }
                    break;
                case RIGHT:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_right_1.png"));
                    if (x < NUM_TILES_X - 1 && !collisionMap[(int) x + 1][(int) y]) {
                        x++;
                        player.setX(x);
                    }
                    break;
                default:
                    break;
            }
            if (e.getCode() == KeyCode.A){
                System.out.println("A was pressed");
            }
            playerImageView.setLayoutX(player.getX() * TILE_SIZE);
            playerImageView.setLayoutY(player.getY() * TILE_SIZE);

            checkForItemPickup(root);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkForItemPickup(Group root) {
        List<Item> pickedUpItems = new ArrayList<>();
        for (Item item : items) {
            if (player.getX() == item.getX() && player.getY() == item.getY()) {
                player.addItem(item);
                pickedUpItems.add(item);
                System.out.println("Item picked up");
                root.getChildren().remove(item.getItemImage());
            }
        }
        items.removeAll(pickedUpItems);
    }

    private void updateInventoryBox() {
        inventoryBox.getChildren().clear();
        Label inventoryLabel = new Label(player.getInventoryAsString());
        inventoryBox.getChildren().add(inventoryLabel);
    }


    private Image[][] loadTileImages(String filePath) {
        Image[][] tileImages = new Image[NUM_TILES_X][NUM_TILES_Y];
        collisionMap = new boolean[NUM_TILES_X][NUM_TILES_Y];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIndex = 0;
            while ((line = reader.readLine()) != null && rowIndex < NUM_TILES_Y) {
                String[] values = line.split(",");
                int columnIndex = 0;
                for (String value : values) {
                    int intValue = Integer.parseInt(value.trim());
                    String imagePath = getImagePathForValue(intValue);
                    tileImages[columnIndex][rowIndex] = new Image(imagePath);
                    if (intValue == 0 || intValue == 1) {
                        collisionMap[columnIndex][rowIndex] = true;
                    }
                    columnIndex++;
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileImages;
    }

    private String getImagePathForValue(int value) {
        switch (value) {
            case 0:
                return "file:src/PokeSmart/Tiles/ground/wall.png";
            case 1:
                return "file:src/PokeSmart/Tiles/ground/water.png";
            case 2:
                return "file:src/PokeSmart/Tiles/ground/sand.png";
            case 3:
                return "file:src/PokeSmart/Tiles/ground/earth.png";
            case 4:
                return "file:src/PokeSmart/Tiles/ground/grass.png";
                // Ajoutez d'autres cas selon vos besoins
            default:
                return null;
        }
    }

    private GridPane createMap(Image[][] tileImages) {
        GridPane gridPane = new GridPane();
        for (int y = 0; y < NUM_TILES_Y; y++) {
            for (int x = 0; x < NUM_TILES_X; x++) {
                ImageView imageView = new ImageView(tileImages[x][y]);
                imageView.setFitWidth(TILE_SIZE);
                imageView.setFitHeight(TILE_SIZE);
                gridPane.add(imageView, x, y);
            }
        }
        return gridPane;
    }

    private void showEntities(ImageView playerImageView, Group root) {
        // player
        playerImageView.setFitWidth(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        playerImageView.setFitHeight(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        root.getChildren().add(playerImageView); // Ajout de l'ImageView du joueur à la scène

        playerImageView.setLayoutX(player.getX() * TILE_SIZE);
        playerImageView.setLayoutY(player.getY() * TILE_SIZE);


        // monster
        ImageView monsterImageView = new ImageView(monster.getMonsterImage());
        monsterImageView.setFitWidth(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        monsterImageView.setFitHeight(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        root.getChildren().add(monsterImageView); // Ajout de l'ImageView du joueur à la scène

        monsterImageView.setLayoutX(monster.getX() * TILE_SIZE);
        monsterImageView.setLayoutY(monster.getY() * TILE_SIZE);


        // npc
        ImageView npcImageView = new ImageView(npc.getImage());
        npcImageView.setFitWidth(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        npcImageView.setFitHeight(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        root.getChildren().add(npcImageView); // Ajout de l'ImageView du joueur à la scène

        npcImageView.setLayoutX(npc.getX() * TILE_SIZE);
        npcImageView.setLayoutY(npc.getY() * TILE_SIZE);


        for (Item item : items) {
            ImageView itemImageView = item.getItemImage();
            itemImageView.setFitWidth(TILE_SIZE);
            itemImageView.setFitHeight(TILE_SIZE);
            root.getChildren().add(itemImageView);
            itemImageView.setLayoutX(item.getX() * TILE_SIZE);
            itemImageView.setLayoutY(item.getY() * TILE_SIZE);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

