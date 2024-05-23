import PokeSmart.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.*;
import java.util.*;


public class Main extends Application {
    private final int TILE_SIZE = 48; // Taille d'une tuile en pixels
    private final int NUM_TILES_X = 16; // Nombre de tuiles en largeur
    private final int NUM_TILES_Y = 12; // Nombre de tuiles en hauteur

    private boolean questAccepted = false;
    private boolean monsterKilled = false;

    private Player player;
    private Monster monster;
    private NPC npc;
    private int[][] collisionMap;
    private List<Item> items;
    private List<Entity> entities;
    private VBox inventoryBox;
    private Label healthPointsLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PokeSmart - world1");
        initCaractersWorld1();
        String worldPath1 = "src/PokeSmart/Tiles/world1.csv";
        GenMap(primaryStage, worldPath1, entities, items);
        updateInventoryBox();
    }

    private void initCaractersWorld1(){
        player = new Player("Popo", 6, 1, 1, 1, 100, 0,100, 2, 100, 1000, 3, "src/PokeSmart/Player/Walking sprites/boy_down_1.png");
        entities = new ArrayList<Entity>();
        monster = new Monster("Papa", 7, 2, 0, 0, 1,0, "OFFENSIVE", 1, 1, 1, 1,"src/PokeSmart/Monster/orc_down_2.png");
        npc = new NPC("Jojo", 7,8,0,0,1,3,0,"src/PokeSmart/NPC/oldman_down_1.png");
        entities.add(monster);
        entities.add(npc);

        items = new ArrayList<Item>();
        items.add(new Item(7,3,"HealPotion", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));
        items.add(new Item(0,11,"WallPotion", "walls are no more a problem", Effet.OVERWALL,1,"src/PokeSmart/Object/potion_grey.png"));
        items.add(new Item(7,5,"SwimPotion", "water is no more a problem", Effet.SWIM,1,"src/PokeSmart/Object/potion_blue.png"));
        items.add(new Item(15,0,"Key", "doors can be opened", Effet.OPENDOOR,1,"src/PokeSmart/Object/key.png"));
        items.add(new Item(14,10,"Door", "go to an other world", Effet.NEWWORLD,1,"src/PokeSmart/Object/door_iron.png"));
    }



    private void GenMap(Stage primaryStage, String worldPath, List<Entity> entities, List<Item> items){
        // Chargement des données depuis le fichier CSV
        Image[][] tileImages = loadTileImages(worldPath);

        // Création de la carte
        GridPane gridPane = createMap(tileImages);

        // Création de la boîte d'inventaire
        inventoryBox = new VBox();

        // Création de la scène
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, NUM_TILES_X * TILE_SIZE + 200, NUM_TILES_Y * TILE_SIZE);
        root.setLeft(gridPane);
        root.setCenter(inventoryBox);

        // Création de la barre de vie
        healthPointsLabel = new Label("Health Points: \n" + player.getHealthPoints());
        updateHealthPointsLabel(root);

        // Création de l'ImageView du joueur
        ImageView playerImageView = player.getImage();
        showEntities(playerImageView, root);

        keySet(scene, playerImageView, root, tileImages, worldPath, primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Player capacities : "+player.getCapacities());
    }



    private Image[][] loadTileImages(String filePath) {
        Image[][] tileImages = new Image[NUM_TILES_X][NUM_TILES_Y];
        collisionMap = new int[NUM_TILES_X][NUM_TILES_Y];
        updateCollisionMap(tileImages, filePath);
        return tileImages;
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



    private void updateInventoryBox() {
        inventoryBox.getChildren().clear();
        Label inventoryLabel = new Label(player.getInventoryAsString());
        inventoryBox.getChildren().add(inventoryLabel);
    }


    private void showEntities(ImageView playerImageView, BorderPane root) {
        // player
        playerImageView.setFitWidth(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        playerImageView.setFitHeight(TILE_SIZE); // Ajustez la taille de l'image selon vos besoins
        root.getChildren().add(playerImageView); // Ajout de l'ImageView du joueur à la scène

        playerImageView.setLayoutX(player.getX() * TILE_SIZE);
        playerImageView.setLayoutY(player.getY() * TILE_SIZE);

        // entities
        for (Entity entity : entities) {
            ImageView entityImageView = entity.getImage();
            entityImageView.setFitWidth(TILE_SIZE);
            entityImageView.setFitHeight(TILE_SIZE);
            root.getChildren().add(entityImageView);
            entityImageView.setLayoutX(entity.getX() * TILE_SIZE);
            entityImageView.setLayoutY(entity.getY() * TILE_SIZE);
        }

        // items
        for (Item item : items) {
            ImageView itemImageView = item.getImage();
            itemImageView.setFitWidth(TILE_SIZE);
            itemImageView.setFitHeight(TILE_SIZE);
            root.getChildren().add(itemImageView);
            itemImageView.setLayoutX(item.getX() * TILE_SIZE);
            itemImageView.setLayoutY(item.getY() * TILE_SIZE);
        }
    }



    private void keySet(Scene scene, ImageView playerImageView, BorderPane root, Image[][] tileImages, String worldPath, Stage primaryStage) {
        scene.setOnKeyPressed(e -> {
            double x = player.getX();
            double y = player.getY();
            switch (e.getCode()) {
                case UP:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_up_1.png"));
                    y -= 1;
                    break;
                case DOWN:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_down_1.png"));
                    y += 1;
                    break;
                case LEFT:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_left_1.png"));
                    x -= 1;
                    break;
                case RIGHT:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_right_1.png"));
                    x += 1;
                    break;
                case I:
                    showInventoryWindow(entities);
                default:
                    return; // Ne rien faire pour d'autres touches
            }

            // Vérifier si la nouvelle position est à l'intérieur de la carte et n'a pas de collision
            if (x >= 0 && x < NUM_TILES_X && y >= 0 && y < NUM_TILES_Y) {
                if (collisionMap[(int) x][(int) y] == 0) {
                    player.setX(x);
                    player.setY(y);
                } else if (collisionMap[(int) x][(int) y] == 1) {
                    if (player.isCanOverWall()) {
                        player.setX(x);
                        player.setY(y);
                    }
                    else {
                        showAlert("Collision alert",null,"You can't go over walls. You have to pick up the WallPotion first");
                    }
                } else if (collisionMap[(int) x][(int) y] == 2) {
                    if (player.isCanSwim()) {
                        player.setX(x);
                        player.setY(y);
                    } else {
                        showAlert("Collision alert", null, "You can't swim. You have to pick up the SwimPotion first");
                        Random random = new Random();
                        int randomNumber = random.nextInt(2); // Générer un nombre aléatoire soit 0 soit 1
                        if (randomNumber == 1) {
                            player.setDestoyed(true);
                        }
                    }
                }
                else {
                    System.out.println("You can't go there");
                }
            }


            checkDestroyedPlayer(primaryStage);

            // Met à jour l'image du joueur sur la carte
            playerImageView.setLayoutX(player.getX() * TILE_SIZE);
            playerImageView.setLayoutY(player.getY() * TILE_SIZE);

            checkForItemPickup(root, tileImages, worldPath, primaryStage);
            checkForMonsterEncounter(root, playerImageView, primaryStage);
            checkForNPCEncounter(root, playerImageView, primaryStage);
        });
    }

    private void showAlert(String title, String header, String phrase) {
        Alert alterWall = new Alert(Alert.AlertType.INFORMATION);
        alterWall.setTitle(title);
        alterWall.setHeaderText(header);
        alterWall.setContentText(phrase);
        alterWall.showAndWait();
    }

    private void checkDestroyedPlayer(Stage primaryStage/*, Entity entity*/) {
        if (player.getDestroyed()) {
            Alert endGame = new Alert(Alert.AlertType.INFORMATION);
            endGame.setTitle("Game Over");
            endGame.setHeaderText(null);
            endGame.setContentText("You are dead !");
            endGame.showAndWait();
            System.out.println("You are dead !");
            primaryStage.close();
        }
        /*if (entity.getDestroyed()) {
            ImageView entityImageView = entity.getImage();
            if (entityImageView != null && entityImageView.getParent() != null) {
                ((Pane) entityImageView.getParent()).getChildren().remove(entityImageView);
            }
        }*/
    }


    private void updateCollisionMap(Image[][] tileImages, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIndex = 0;
            while ((line = reader.readLine()) != null && rowIndex < NUM_TILES_Y) {
                String[] values = line.split(",");
                int columnIndex = 0;
                for (String value : values) {
                    int intValue = Integer.parseInt(value.trim());
                    String imagePath = getImagePathForValue(intValue);
                    tileImages[columnIndex][rowIndex] = new Image(imagePath); //imageView.getImage();
                    if (intValue == 0) { // collision avec les murs
                        collisionMap[columnIndex][rowIndex] = 1;
                    } else if  (intValue == 1) { // collision avec l'eau
                        collisionMap[columnIndex][rowIndex] = 2;
                    }
                    else {
                        collisionMap[columnIndex][rowIndex] = 0;
                    }
                    columnIndex++;
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showInventoryWindow(List<Entity> entities) {
        // stage for inventory window
        Stage inventoryStage = new Stage();
        inventoryStage.setTitle("Player inventory");
        inventoryStage.setWidth(500);
        inventoryStage.setHeight(500);

        // GridPane for inventory items
        GridPane inventoryGridPane = new GridPane();
        inventoryGridPane.setHgap(10);
        inventoryGridPane.setVgap(10);

        Scene inventoryScene = new Scene(inventoryGridPane);

        // Create labels for the player's name, current money, and life
        Label nameLabel = new Label("Name : " + player.getName());
        Label moneyLabel = new Label("Money : " + player.getMoney());
        Label lifeLabel = new Label("Life : " + player.getHealthPoints());

        // Add the labels to the grid pane
        inventoryGridPane.add(nameLabel, 0, 0);
        inventoryGridPane.add(moneyLabel, 1, 0);
        inventoryGridPane.add(lifeLabel, 2, 0);

        int rowIndex = 1;
        for (Item item : player.getInventory()) {
            // Create an ImageView for the inventory item
            ImageView potionImageView = item.getImage();

            // Create a Label for the name of the inventory item
            Label potionNameLabel = new Label(item.getItemName());

            Button useButton = new Button("Use");
            useButton.setOnAction(e -> {
                item.useItem(player);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Item Used");
                alert.setHeaderText(null);
                alert.setContentText(item.getItemDescription());
                alert.showAndWait();
            });

            // Add the Button, the name Label, and the price Label to the grid pane
            inventoryGridPane.add(useButton, 0, rowIndex);
            inventoryGridPane.add(potionNameLabel, 1, rowIndex);
            inventoryGridPane.add(potionImageView, 2, rowIndex);
            rowIndex++;
        }
        inventoryStage.setScene(inventoryScene);
        inventoryStage.show();
    }



    private void checkForItemPickup(BorderPane root, Image[][] tileImages, String filePath, Stage primaryStage) {
        List<Item> pickedUpItems = new ArrayList<>();
        for (Item item : items) {
            if (player.getX() == item.getX() && player.getY() == item.getY()) {
                if (item.getEffet() != Effet.NEWWORLD){
                    player.addItem(item);
                    pickedUpItems.add(item);
                    System.out.println("Item picked up");
                    root.getChildren().remove(item.getImage());
                    updateInventoryBox(); // voir pour le bouton
                    System.out.println(player.getCapacities());
                }
                if (item.getItemName() == "Key") {
                    System.out.println("You picked up a key !");
                    for (Item item1 : items) {
                        if ((player.getDiscoverNewWorld() == 1) && "Door".equals(item1.getItemName())) {//("Door".equals(item1.getItemName())) {
                            root.getChildren().remove(item1.getImage()); // Remove the old image from the scene
                            ImageView newImage = new ImageView("file:src/PokeSmart/Object/door.png"); // Create a new ImageView
                            newImage.setFitWidth(TILE_SIZE);
                            newImage.setFitHeight(TILE_SIZE);
                            newImage.setLayoutX(item1.getX() * TILE_SIZE); // Set the x-coordinate of the ImageView
                            newImage.setLayoutY(item1.getY() * TILE_SIZE); // Set the y-coordinate of the ImageView
                            item1.setImage(newImage); // Update the image of the item
                            root.getChildren().add(item1.getImage()); // Add the new image to the scene
                            System.out.println("boucle1");
                        }
                    }
                }
                if (player.getDiscoverNewWorld() == 1 && item.getEffet() == Effet.NEWWORLD) { // condition sur la clé
                    System.out.println("You finish first world !");
                    primaryStage.close();
                    Alert alertNewWorld = new Alert(Alert.AlertType.INFORMATION);
                    alertNewWorld.setTitle("Congratulations !");
                    alertNewWorld.setHeaderText(null);
                    alertNewWorld.setContentText("You finish first world !");
                    alertNewWorld.showAndWait();
                    createNewWorld();
                }
                if (player.getDiscoverNewWorld() == 0 && item.getEffet() == Effet.NEWWORLD) {
                    System.out.println("You need a key to open the door");
                    Alert alertNewWorld1 = new Alert(Alert.AlertType.INFORMATION);
                    alertNewWorld1.setTitle("Information Dialog");
                    alertNewWorld1.setHeaderText(null);
                    alertNewWorld1.setContentText("You need a key to open the door ! (Your player is behind the door.");
                    alertNewWorld1.showAndWait();
                }
                if (item.getEffet() == Effet.DEATH) {
                    System.out.println("You are dead !");
                    primaryStage.close();
                }
            }
        }
        items.removeAll(pickedUpItems);

        updateCollisionMap(tileImages, filePath); // Mettre à jour la carte des collisions
    }



    private void checkForNPCEncounter(BorderPane root, ImageView playerImageView, Stage primaryStage) {
        if (player.equals(npc)) { //if (player.getX() == npc.getX() && player.getY() == npc.getY()) {
            System.out.println("NPC encountered");

            if (questAccepted) {
                if (monsterKilled) {
                    System.out.println("Quest completed");
                    showAlert("Quest completed", null, "You completed the quest! You can go to the next world.");
                    player.setDiscoverNewWorld(1);
                    return;
                }
                showAlert("Quest not completed", null, "You have to kill the Monster first.");
                return;
            }
            else {
                showQuestDialog();
                return;
            }
        }
    }

    private void showQuestDialog() {
        // Create a dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Dialogue with NPC");
        dialog.setHeaderText("NPC: Hello, I have a quest for you. Do you accept?");

        // Add buttons to the dialog
        ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.YES);
        ButtonType refuseButtonType = new ButtonType("Refuse", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, refuseButtonType);

        // Handle the result
        dialog.setResultConverter(buttonType -> {
            if (buttonType == acceptButtonType) {
                questAccepted = true;
                showAlert("Quest accepted", null, "You accepted the quest! You have to kill the Monster.");
                return "Accepted";
            } else if (buttonType == refuseButtonType) {
                // Handle the player refusing the quest
                System.out.println("You refused the quest.");
                return "Refused";
            }
            return null;
        });

        // Show the dialog
        Optional<String> result = dialog.showAndWait();
    }


    private void checkForMonsterEncounter(BorderPane root, ImageView playerImageView, Stage primaryStage) {
        if (player.equals(monster)) { //if (player.getX() == monster.getX() && player.getY() == monster.getY()) {
            System.out.println("Monster encountered");


        }
    }

    private void showMonsterDialog(BorderPane root) {
        // Create a dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Combat with Monster");
        dialog.setHeaderText("Monster: I will defeat you! What will you do?");

        // Add buttons to the dialog
        ButtonType attackButtonType = new ButtonType("Attack", ButtonBar.ButtonData.YES);
        ButtonType runButtonType = new ButtonType("Run", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(attackButtonType, runButtonType);

        // Handle the result
        dialog.setResultConverter(buttonType -> {
            if (buttonType == attackButtonType) {
                // Handle the player attacking the monster
                // You can add your combat logic here
                player.setHealthPoints(50);
                updateHealthPointsLabel(root);
                System.out.println("You attacked the monster!");
                return "Attacked";
            } else if (buttonType == runButtonType) {
                // Handle the player running away
                // You can add your escape logic here
                System.out.println("You ran away from the monster.");
                return "Ran";
            }
            return null;
        });

        // Show the dialog
        Optional<String> result = dialog.showAndWait();
    }



    private void createNewWorld() {
        // Par exemple, vous pouvez appeler la méthode GenMap avec un nouveau fichier de carte
        String newWorldPath = "src/PokeSmart/Tiles/world2.csv";
        Stage newWorldStage = new Stage();
        newWorldStage.setTitle("PokeSmart - world2");
        initCaractersWorld2();
        GenMap(newWorldStage, newWorldPath, entities, items);
    }



    private void initCaractersWorld2(){
        entities.clear(); // vérifier que ça supprime bien les entités du monde précédent
        // supprimer tous les items précédents et regarder le bug quand on change de monde et récupère un item ça change la carte
        entities = new ArrayList<Entity>();
        entities.add(new Monster("BatMan", 7, 2, 0, 0, 1,0, "OFFENSIVE", 1, 1, 1, 1,"src/PokeSmart/Monster/bat_down_2.png"));
        entities.add(new Monster("Skeleton", 10, 2, 0, 0, 1,0, "OFFENSIVE", 1, 1, 1, 1,"src/PokeSmart/Monster/skeletonlord_down_1.png"));

        items = new ArrayList<Item>();
        items.add(new Item(7,3,"HealPotion", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));
        items.add(new Item(7,7,"Car", "this can heal you", Effet.VICTORY,1,"src/PokeSmart/Items/911-removebg-preview.png"));
    }



    private void updateHealthPointsLabel(BorderPane root) {
        if (healthPointsLabel == null) {
            healthPointsLabel = new Label();
        }
        healthPointsLabel.setText("Health Points: \n" + player.getHealthPoints());
        root.setRight(healthPointsLabel);
    }


    private String getImagePathForValue(int value) {
        return switch (value) {
            case 0 -> "file:src/PokeSmart/Tiles/ground/wall.png";
            case 1 -> "file:src/PokeSmart/Tiles/ground/water.png";
            case 2 -> "file:src/PokeSmart/Tiles/ground/sand.png";
            case 3 -> "file:src/PokeSmart/Tiles/ground/earth.png";
            case 4 -> "file:src/PokeSmart/Tiles/ground/grass.png";
            default -> null;
        };
    }



    public static void main(String[] args) {
        launch(args);
    }
}

