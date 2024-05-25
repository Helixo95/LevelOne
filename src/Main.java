import PokeSmart.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.*;
import java.util.*;


public class Main extends Application {
    private final int TILE_SIZE = 48; // Taille d'une tuile en pixels
    private final int NUM_TILES_X = 16; // Nombre de tuiles en largeur
    private final int NUM_TILES_Y = 12; // Nombre de tuiles en hauteur

    private boolean monsterKilled = false;
    private boolean isFirstQuestSucceded = false;
    private boolean isInInventory = false;

    private Player player;
    private Monster orc;
    private Monster skeleton;
    private NPC npc;
    private int[][] collisionMap;
    private List<Item> items;
    private List<Entity> entities;
    private List<Monster> monsters;
    private VBox inventoryBox;
    private Label healthPointsLabel;
    private Label monsterHealthPointsLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PokeSmart - world1");
        initCaractersWorld1();
        String worldPath1 = "src/PokeSmart/Tiles/world1.csv";
        GenMap(primaryStage, worldPath1, entities, items, orc);
        updateInventoryBox();
    }

    private void initCaractersWorld1(){
        player = new Player("Popo", 6, 1, 1, 1, 100,150, 50, 50, 10000, 3, "src/PokeSmart/Player/Walking sprites/boy_down_1.png");
        player.setInventory(new ArrayList<Item>());
        entities = new ArrayList<Entity>();
        monsters = new ArrayList<Monster>();
        orc = new Monster("Papa", 7, 2, 0, 0, 100, MonsterType.ORC, 1, 30, 100,"src/PokeSmart/Monster/orc_down_2.png");
        orc.TypeMonster(orc);
        npc = new NPC("Jojo", 7,8,0,0,NPCType.QUEST,"src/PokeSmart/NPC/oldman_down_1.png");
        entities.add(orc);
        entities.add(npc);
        monsters.add(orc);

        items = new ArrayList<Item>();

        player.addItem(new Item(7,4,"HealPotionTest", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));

        items.add(new Item(7,3,"HealPotion1", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));
        items.add(new Item(7,4,"HealPotion2", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));
        items.add(new Item(0,11,"WallPotion", "walls are no more a problem", Effet.OVERWALL,1,"src/PokeSmart/Object/potion_grey.png"));
        items.add(new Item(7,1,"WallPotion", "walls are no more a problem", Effet.OVERWALL,1,"src/PokeSmart/Object/potion_grey.png"));
        //items.add(new Item(7,5,"SwimPotion", "water is no more a problem", Effet.SWIM,1,"src/PokeSmart/Object/potion_blue.png"));
        items.add(new Item(15,0,"Key", "doors can be opened", Effet.OPENDOOR,1,"src/PokeSmart/Object/key.png"));
        items.add(new Item(14,10,"Door", "go to an other world", Effet.NEWWORLD,1,"src/PokeSmart/Object/door_iron.png"));
    }



    private void GenMap(Stage primaryStage, String worldPath, List<Entity> entities, List<Item> items, Monster monster){
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

        updateInventoryBox();
        updateHealthPointsLabel(root, monster);

        // Création de l'ImageView du joueur
        ImageView playerImageView = player.getImage();
        showEntities(playerImageView, root);

        keySet(scene, playerImageView, root, tileImages, worldPath, primaryStage, monster);

        primaryStage.setScene(scene);
        primaryStage.show();
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



    private void keySet(Scene scene, ImageView playerImageView, BorderPane root, Image[][] tileImages, String worldPath, Stage primaryStage, Monster monster) {
        scene.setOnKeyPressed(e -> {
            double x = player.getX();
            double y = player.getY();
            switch (e.getCode()) {
                case Z:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_up_1.png"));
                    y -= 1;
                    break;
                case S:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_down_1.png"));
                    y += 1;
                    break;
                case Q:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_left_1.png"));
                    x -= 1;
                    break;
                case D:
                    playerImageView.setImage(new Image("file:src/PokeSmart/Player/Walking sprites/boy_right_1.png"));
                    x += 1;
                    break;
                case I:
                    showInventoryWindow(monster, root);
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
                        player.showAlert("Collision alert",null,"You can't go over walls. You have to pick up the WallPotion first");
                    }
                } else if (collisionMap[(int) x][(int) y] == 2) {
                    if (player.isCanSwim()) {
                        player.setX(x);
                        player.setY(y);
                    } else {
                        player.showAlert("Collision alert", null, "You can't swim. You have to pick up the SwimPotion first");
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

            checkDestroyedPlayer(primaryStage, orc, root);

            // Met à jour l'image du joueur sur la carte
            playerImageView.setLayoutX(player.getX() * TILE_SIZE);
            playerImageView.setLayoutY(player.getY() * TILE_SIZE);

            checkForItemPickup(root, tileImages, worldPath, primaryStage);
            checkForMonsterEncounter(root, monster, primaryStage);
            checkForNPCEncounter(root, playerImageView, primaryStage);
        });
    }

    private void checkDestroyedPlayer(Stage primaryStage, Entity entity, BorderPane root) {
        if (player.getHealthPoints() <= 0) {
            player.setDestoyed(true);
        }
        if (player.getDestroyed()) {
            player.showAlert("Game Over", null, "You are dead !");
            System.out.println("You are dead !");
            primaryStage.close();
        }
        if (entity.getDestroyed()) {
            ImageView entityImageView = entity.getImage();
            if (entityImageView != null && entityImageView.getParent() != null) {
                root.getChildren().remove(entity.getImage());
            }
        }
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


    private void showInventoryWindow(Monster monster, BorderPane root) {
        // stage for inventory window
        Stage inventoryStage = new Stage();
        inventoryStage.setTitle("Player inventory");
        inventoryStage.setWidth(400);
        inventoryStage.setHeight(400);

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

        // Method to update the labels
        Runnable updateLabels = () -> {
            nameLabel.setText("Name : " + player.getName());
            moneyLabel.setText("Money : " + player.getMoney());
            lifeLabel.setText("Life : " + player.getHealthPoints());
        };

        int rowIndex = 1;
        for (Item item : player.getInventory()) {
            System.out.println("Inventory bag : ");
            System.out.println("Item : " + item.getItemName());
            System.out.println("Q : " + item.getQuantity());

            // Create an ImageView for the inventory item
            ImageView itemImageView = item.getImage();
            itemImageView.setFitWidth(TILE_SIZE);
            itemImageView.setFitHeight(TILE_SIZE);
            itemImageView.setLayoutX(item.getX() * TILE_SIZE);
            itemImageView.setLayoutY(item.getY() * TILE_SIZE);

            // Create a Label for the name of the inventory item
            Label potionNameLabel = new Label(item.getItemName());
            Label quantityLabel = new Label("Quantity : " + item.getQuantity());

            Button useButton = new Button("Use");
            useButton.setOnAction(e -> {
                item.useItem(player, monster);
                updateDoor(root);
                item.setQuantity(item.getQuantity() - 1);
                item.showAlert("Item Used", null, item.getItemDescription());
                if (item.getQuantity() == 0) {
                    inventoryGridPane.getChildren().remove(useButton);
                    inventoryGridPane.getChildren().remove(potionNameLabel);
                    inventoryGridPane.getChildren().remove(quantityLabel);
                    inventoryGridPane.getChildren().remove(itemImageView);
                    player.getInventory().remove(item);
                } else {
                    quantityLabel.setText("Quantity : " + item.getQuantity());
                }
                updateLabels.run();
                updateHealthPointsLabel(root, monster);
                updateInventoryBox();
            });

            // Add the Button, the name Label, and the price Label to the grid pane
            inventoryGridPane.add(useButton, 0, rowIndex);
            inventoryGridPane.add(potionNameLabel, 1, rowIndex);
            inventoryGridPane.add(quantityLabel, 2, rowIndex);
            inventoryGridPane.add(itemImageView, 3, rowIndex);
            rowIndex++;
        }
        inventoryStage.setScene(inventoryScene);
        inventoryStage.show();
    }



    private void checkForItemPickup(BorderPane root, Image[][] tileImages, String filePath, Stage primaryStage) {
        Item pickedUpItems = null;
        isInInventory = false;
        for (Item item : items) {
            if (player.getX() == item.getX() && player.getY() == item.getY()) {
                System.out.println("Check for items : ");
                System.out.println("Item : " + item.getItemName());
                System.out.println("Q : " + item.getQuantity());
                pickedUpItems = item;

                if (item.getEffet() != Effet.NEWWORLD) {
                    System.out.println("Ajout d'item : "+item.getItemName());
                    if(item.getQuantity() == 1) {
                        for (Item item1 : player.getInventory()) {
                            if (item1.getEffet().equals(item.getEffet())) {
                                item1.setQuantity(item1.getQuantity() + 1);
                                isInInventory = true;
                                break;
                            }
                        }
                        if (!isInInventory) {
                            player.addItem(item);
                        }
                    }
                }
                if (item.getItemName() == "Key") {
                    System.out.println("You picked up a key !");
                    //item.setQuantity(1);
                    //player.addItem(item);
                    updateDoor(root);
                }
                if (player.getDiscoverNewWorld() == 1 && item.getEffet() == Effet.NEWWORLD) { // condition sur la clé
                    System.out.println("You finish first world !");
                    root.getChildren().removeAll();
                    primaryStage.close();
                    player.showAlert("Congratulations !", null, "You finish first world !");
                    createNewWorld(primaryStage, root);
                }
                if (player.getDiscoverNewWorld() == 0 && item.getEffet() == Effet.NEWWORLD) {
                    System.out.println("You need a key to open the door");
                    item.showAlert("Information Dialog", null, "You need a key to open the door ! (Your player is behind the door.");
                }
                if (item.getEffet() == Effet.DEATH) {
                    System.out.println("You are dead !");
                    primaryStage.close();
                }
            }
        }
        if (pickedUpItems != null && pickedUpItems.getEffet() != Effet.NEWWORLD) {
            items.remove(pickedUpItems);
            root.getChildren().remove(pickedUpItems.getImage());
        }
        updateInventoryBox();
        updateCollisionMap(tileImages, filePath); // Mettre à jour la carte des collisions
    }


    private void updateDoor(BorderPane root) {
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
            }
        }
    }



    private void checkForNPCEncounter(BorderPane root, ImageView playerImageView, Stage primaryStage) {
        if (entities.contains(npc) && player.equals(npc) /*&& !npc.isFirstQuestAccepted()*/) {
            System.out.println("NPC encountered");
            if (npc.getNPCType().equals(NPCType.QUEST)) {
                System.out.println("Quest NPC encountered");
                firstQuest(root);
            } else if (npc.getNPCType().equals(NPCType.VILLAGER)) {
                System.out.println("Villager NPC encountered");
                npc.showAlert("Villager encountered", null, "Hello " + player.getName() + " I am " + npc.getName());
            } else if (npc.getNPCType().equals(NPCType.SPECIAL)) {
                System.out.println("Special NPC encountered");
                npc.showAlert("Special NPC encountered", null, "Hello " + player.getName() + ", I am " + npc.getName()+ " and I have a gift for you !");
                npc.giveItem(player, new Item(7,5,"Sword", "water is no more a problem", Effet.ATTAQUEPLUS,1,"src/PokeSmart/Object/sword_normal.png"));
                player.showAlert("Gift received", null, "You received a gift from "+npc.getName()+" !");
            }
        }
    }


    private void firstQuest(BorderPane root) {
        if (npc.isFirstQuestAccepted()) {
            if (monsterKilled) {
                System.out.println("Quest completed");
                npc.showAlert("Quest completed", null, "You completed the quest! Pick up the key and go to the next world.");
                Item seaPotion = new Item(7,5,"SwimPotion", "water is no more a problem", Effet.SWIM,1,"src/PokeSmart/Object/potion_blue.png");
                items.add(seaPotion);

                // afficher l'item sur la carte
                ImageView seaPotionImageView = seaPotion.getImage();
                seaPotionImageView.setFitWidth(TILE_SIZE);
                seaPotionImageView.setFitHeight(TILE_SIZE);
                root.getChildren().add(seaPotionImageView);
                seaPotionImageView.setLayoutX(seaPotion.getX() * TILE_SIZE);
                seaPotionImageView.setLayoutY(seaPotion.getY() * TILE_SIZE);

                // supprime le pnj
                root.getChildren().remove(npc.getImage());
                System.out.println("FirstQuest : true");
                monsterKilled = false;
                isFirstQuestSucceded = true;
                return;
            }
            npc.showAlert("Quest not completed", null, "You have to kill the Monster first.");
            return;
        }
        else {
            npc.showQuestDialog(player);
            return;
        }
    }


    private void checkForMonsterEncounter(BorderPane root, Monster monster, Stage stage) {
        if (entities.contains(monster) && player.equals(monster)) { //if (player.getX() == monster.getX() && player.getY() == monster.getY()) {
            System.out.println("Monster encountered");
            if (monster.getMonsterType().equals(MonsterType.ORC)) {
                System.out.println("Orc Monster encountered");
                if (!npc.isFirstQuestAccepted() && !isFirstQuestSucceded) {
                    orc.showAlert("Monster encountered", null, "You have to accept the quest first.");
                    return;
                } else if (npc.isFirstQuestAccepted() && !monsterKilled) {
                    showMonsterDialog(root, monster);
                }
            } else if (monster.getMonsterType().equals(MonsterType.SKELETON)) {
                System.out.println("Skeleton Monster encountered");
                showMonsterDialog(root, monster);
                //player.attack(monster);
                if (monster.getHealthPoints() <= 0) {
                    monsterKilled = true;
                    monster.showAlert("Monster defeated", null, "You defeated the boss !");
                    player.showAlert("Congratulations !", null, "You finished the game !");
                    root.getChildren().remove(monster.getImage());
                    stage.close();
                }
            }
        }
        for (Monster monster1 : monsters) {
            if (monster1.getMonsterType().equals(MonsterType.BAT) && player.equals(monster1)) {
                System.out.println("Bat Monster encountered");
                //showMonsterDialog(root, monster1);
            }
        }
    }

    private void showMonsterDialog(BorderPane root, Monster monster) {
        // Create a dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Combat with Monster");
        dialog.setHeaderText("Hi "+player.getName()+", prepare to die, I will defeat you ! What will you do ?");

        // Add buttons to the dialog
        ButtonType attackButtonType = new ButtonType("Attack", ButtonBar.ButtonData.YES);
        ButtonType runButtonType = new ButtonType("Run", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(attackButtonType, runButtonType);

        // Handle the result
        dialog.setResultConverter(buttonType -> {
            if (buttonType == attackButtonType) {
                // Handle the player attacking the monster
                player.attack(monster);
                updateHealthPointsLabel(root, monster);
                if (monster.getHealthPoints() <= 0) {
                    monsterKilled = true;
                    monster.showAlert("Monster defeated", null, "You defeated the monster!");
                    root.getChildren().remove(monster.getImage());
                    return "Defeated";
                }
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



    private void createNewWorld(Stage primaryStage, BorderPane root) {
        // Par exemple, vous pouvez appeler la méthode GenMap avec un nouveau fichier de carte
        String newWorldPath = "src/PokeSmart/Tiles/world2.csv";
        Stage newWorldStage = new Stage();
        newWorldStage.setTitle("PokeSmart - world2");
        initCaractersWorld2(primaryStage, root);
        GenMap(newWorldStage, newWorldPath, entities, items, skeleton);
    }



    private void initCaractersWorld2(Stage primaryStage, BorderPane root){
        for (Item item : items) {
            root.getChildren().remove(item.getImage());
        }
        for (Entity entity : entities) {
            root.getChildren().remove(entity.getImage());
        }
        for (Monster monster : monsters) {
            root.getChildren().remove(monster.getImage());
        }

        entities.clear(); // vérifier que ça supprime bien les entités du monde précédent
        entities = null;
        monsters.clear();
        monsters = null;
        // supprimer tous les items précédents et regarder le bug quand on change de monde et récupère un item ça change la carte
        checkDestroyedPlayer(primaryStage, npc, root);

        entities = new ArrayList<Entity>();
        monsters = new ArrayList<Monster>();
        entities.add(new Monster("BatMan", 7, 3, 0, 0, 1, MonsterType.BAT, 1, 1, 1,"src/PokeSmart/Monster/bat_down_2.png"));
        skeleton = new Monster("Skeleton", 10, 2, 0, 0, 400, MonsterType.SKELETON, 1, 150, 200, "src/PokeSmart/Monster/skeletonlord_down_1.png");
        entities.add(skeleton);
        npc = new NPC("Momo", 2,4,0,0,NPCType.SPECIAL,"src/PokeSmart/NPC/merchant_down_1.png");
        entities.add(npc);
        monsters.add(new Monster("BatMan", 7, 3, 0, 0, 1, MonsterType.BAT, 1, 1, 1,"src/PokeSmart/Monster/bat_down_2.png"));
        monsters.add(skeleton);

        items = new ArrayList<Item>();
        items.add(new Item(7,4,"HealPotion", "this can heal you", Effet.HEAL,1,"src/PokeSmart/Object/potion_red.png"));
        items.add(new Item(7,7,"Car", "this can heal you", Effet.VICTORY,1,"src/PokeSmart/Items/911-removebg-preview.png"));
    }

    private void updateHealthPointsLabel(BorderPane root, Monster monster) {
        if (healthPointsLabel == null) {
            healthPointsLabel = new Label();
        }
        healthPointsLabel.setText("Your HP : \n" + player.getHealthPoints());

        if (monsterHealthPointsLabel == null) {
            monsterHealthPointsLabel = new Label();
        }
        monsterHealthPointsLabel.setText("Monster HP : \n" + monster.getHealthPoints());

        if (monster.getHealthPoints() <= 0) {
            VBox vBox = new VBox();
            vBox.getChildren().add(healthPointsLabel);
            vBox.setAlignment(Pos.TOP_RIGHT);
            root.setRight(vBox);
        }
        else {
            // Ajoutez les labels des points de vie du joueur et du monstre au root
            VBox vBox = new VBox();
            vBox.getChildren().addAll(healthPointsLabel, monsterHealthPointsLabel);
            vBox.setAlignment(Pos.TOP_RIGHT);
            root.setRight(vBox);
        }
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



