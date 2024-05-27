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

    private boolean monsterKilled = false; // voir si le monstre est tué
    private boolean isFirstQuestSucceded = false; // voir si la première quête est réussie
    private boolean isInInventory = false; // voir si l'item est dans l'inventaire

    private Player player;
    private Monster orc;
    private Monster skeleton;
    private NPC npc;
    private NPC npcQ2;
    private int[][] collisionMap;
    private List<Item> items;
    private List<Item> itemsNPC;
    private List<Entity> entities;
    private List<Monster> monsters;
    private VBox inventoryBox;
    private Label healthPointsLabel;
    private Label monsterHealthPointsLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PokeSmart - world1");
        initCaractersWorld1(); // initialisation des personnages, monstres, pnj et items du monde 1
        String worldPath1 = "resources/Tiles/world1.csv";
        GenMap(primaryStage, worldPath1, orc); // génération de la carte du monde 1 et appel aux autres fonctions
        updateInventoryBox(); // mise à jour de la boîte d'inventaire qui s'affiche à droite de la carte
        Random rand = new Random();
        int randomNumber = rand.nextInt(2) * 2 - 1; // Génère aléatoirement 0 ou 1, puis le transforme en -1 ou 1
        System.out.println("Random number : " + randomNumber);
    }

    /**
     * initialisation des caractères du monde 1
     */
    private void initCaractersWorld1() {
        player = new Player("Sasha", 6, 1, 1, 1, 100,150, 50, 50, 10000, 3, "resources/Player/Walking sprites/boy_down_1.png");
        player.setInventory(new ArrayList<Item>());
        entities = new ArrayList<Entity>();
        monsters = new ArrayList<Monster>();
        orc = new Monster("Jabba", 7, 2, 0, 0, 100, MonsterType.ORC, 1, 30, 100,"resources/Monster/orc_down_2.png");
        orc.TypeMonster(orc);
        npc = new NPC("Le sage", 7,8,0,0,NPCType.QUEST,"resources/NPC/oldman_down_1.png");
        entities.add(orc);
        entities.add(npc);
        monsters.add(orc);

        npcQ2 = new NPC("Item lister", 6,5,0,0,NPCType.LISTITEM,"resources/NPC/bigrock.png");
        entities.add(npcQ2);

        npcQ2.setInventory(new ArrayList<Item>());

        /*Random random = new Random();
        int randomNumber = random.nextInt(2); // Générer aléatoirement 0 ou 1
        if (randomNumber == 1) {*/
            npcQ2.addItem(new Item(7,5,"HealPotion", "this can heal you", Effet.HEAL,1,"resources/Object/potion_red.png"));
            npcQ2.addItem(new Item(7,6,"HealPotion", "this can heal you", Effet.OVERWALL,1,"resources/Object/potion_grey.png"));
        //}



        items = new ArrayList<Item>();

        player.addItem(new Item(7,4,"HealPotionStart", "this can heal you", Effet.HEAL,1,"resources/Object/potion_red.png"));

        // items questions examen
        player.addItem(new Item(7,5,"Show NPC Inventory", "You can see NPC Inventory", Effet.SHOWINVENTORY,1,"resources/Items/HealPotion.png"));
        player.addItem(new Item(7,6,"Téléportation dans le monde", "Téléportation", Effet.TELEPORTATION,10,"resources/Items/Pistol.PNG"));
        player.addItem(new Item(7,7,"RIP le monstre", "Absorbe les pv du monstre", Effet.ABSORB,1,"resources/Object/axe.png"));
        player.addItem(new Item(7,8,"Rob", "Rob l'item du joueur", Effet.ROB,1,"resources/Object/coin_bronze.png"));

        items.add(new Item(7,7,"911", "Vroum vroum", Effet.VICTORY,1,"resources/Items/911-removebg-preview.png"));




        items.add(new Item(7,3,"HealPotion1", "this can heal you", Effet.HEAL,1,"resources/Object/potion_red.png"));
        items.add(new Item(7,4,"HealPotion2", "this can heal you", Effet.HEAL,1,"resources/Object/potion_red.png"));
        items.add(new Item(0,11,"WallPotion", "walls are no more a problem", Effet.OVERWALL,1,"resources/Object/potion_grey.png"));
        items.add(new Item(15,0,"Key", "doors can be opened", Effet.OPENDOOR,1,"resources/Object/key.png"));
        items.add(new Item(14,10,"Door", "go to an other world", Effet.NEWWORLD,1,"resources/Object/door_iron.png"));
    }


    /**
     * Génère la carte du monde et fait appel aux autres fonctions
     *
     * @param primaryStage
     * @param worldPath
     * @param monster
     */
    private void GenMap(Stage primaryStage, String worldPath, Monster monster){
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

        // Création du label des points de vie du joueur et du monstre et mise à jour de l'inventaire
        updateInventoryBox();
        updateHealthPointsLabel(root, monster);

        // Création de l'ImageView du joueur
        ImageView playerImageView = player.getImage();
        showEntities(playerImageView, root);

        // Gestion des touches du clavier et appel aux fonctions nécessaires au jeu
        keySet(scene, playerImageView, root, tileImages, worldPath, primaryStage, monster);

        // Affichage de la scène
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Permet de charger les tiles et d'instancier la carte de collision
     *
     * @param filePath
     * @return
     */
    private Image[][] loadTileImages(String filePath) {
        Image[][] tileImages = new Image[NUM_TILES_X][NUM_TILES_Y];
        collisionMap = new int[NUM_TILES_X][NUM_TILES_Y];
        updateCollisionMap(tileImages, filePath);
        return tileImages;
    }



    /**
     * Crée la carte du monde et l'affiche
     *
     * @param tileImages
     * @return
     */
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


    /**
     * Met à jour l'inventaire du joueur
     */
    private void updateInventoryBox() {
        inventoryBox.getChildren().clear();
        Label inventoryLabel = new Label(player.getInventoryAsString());
        inventoryBox.getChildren().add(inventoryLabel);
    }


    /**
     * Affiche les entités sur la carte (items, joueur, pnj, monstres)
     *
     * @param playerImageView
     * @param root
     */
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


    /**
     * Met à jour l'image du personnage sur la carte
     * Fait appel aux fonctions de collision
     *
     * @param scene
     * @param playerImageView
     * @param root
     * @param tileImages
     * @param worldPath
     * @param primaryStage
     * @param monster
     */
    private void keySet(Scene scene, ImageView playerImageView, BorderPane root, Image[][] tileImages, String worldPath, Stage primaryStage, Monster monster) {
        scene.setOnKeyPressed(e -> { // Gestion des touches du clavier
            double x = player.getX();
            double y = player.getY();
            switch (e.getCode()) {
                case Z:
                    playerImageView.setImage(new Image("file:resources/Player/Walking sprites/boy_up_1.png"));
                    y -= 1;
                    break;
                case S:
                    playerImageView.setImage(new Image("file:resources/Player/Walking sprites/boy_down_1.png"));
                    y += 1;
                    break;
                case Q:
                    playerImageView.setImage(new Image("file:resources/Player/Walking sprites/boy_left_1.png"));
                    x -= 1;
                    break;
                case D:
                    playerImageView.setImage(new Image("file:resources/Player/Walking sprites/boy_right_1.png"));
                    x += 1;
                    break;
                case I:
                    showInventoryWindow(player, monster, root, primaryStage);
                default:
                    return; // Ne rien faire pour d'autres touches
            }

            // Vérifier si la nouvelle position est à l'intérieur de la carte et n'a pas de collision
            if (x >= 0 && x < NUM_TILES_X && y >= 0 && y < NUM_TILES_Y) {
                if (collisionMap[(int) x][(int) y] == 0) { // si la case est vide (terre, sable ou herbe)
                    player.setX(x);
                    player.setY(y);
                } else if (collisionMap[(int) x][(int) y] == 1) {
                    if (player.isCanOverWall()) { // si le joueur peut passer par dessus les murs
                        player.setX(x);
                        player.setY(y);
                    }
                    else {
                        player.showAlert("Collision alert",null,"You can't go over walls. You have to pick up the WallPotion first");
                    }
                } else if (collisionMap[(int) x][(int) y] == 2) {
                    if (player.isCanSwim()) { // si le joueur peut nager
                        player.setX(x);
                        player.setY(y);
                    } else {
                        player.showAlert("Collision alert", null, "You can't swim. You have to pick up the SwimPotion first");
                        Random random = new Random();
                        int randomNumber = random.nextInt(2); // Générer aléatoirement 0 ou 1
                        if (randomNumber == 1) {
                            player.setDestoyed(true); // le joueur est mort (noyé dans ce cas)
                        }
                    }
                }
                else {
                    System.out.println("You can't go there");
                }
            }

            checkDestroyedPlayer(primaryStage, orc, root); // Vérifie si le joueur est mort (également le monstre

            // Met à jour l'image du joueur sur la carte
            playerImageView.setLayoutX(player.getX() * TILE_SIZE);
            playerImageView.setLayoutY(player.getY() * TILE_SIZE);

            checkForItemPickup(root, tileImages, worldPath, primaryStage); // Vérifie si le joueur a ramassé un item
            checkForMonsterEncounter(root, monster, primaryStage); // Vérifie si le joueur rencontre un monstre
            checkForNPCEncounter(root, playerImageView, primaryStage); // Vérifie si le joueur rencontre un pnj
        });
    }


    /**
     * Vérifie sur le joueur ou le monstre est détruit
     *
     * @param primaryStage
     * @param entity
     * @param root
     */
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


    /**
     * Met à jour la carte de collision en fonction des valeurs du fichier CSV
     *
     * @param tileImages
     * @param filePath
     */
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


    /**
     * Affiche la fenêtre de l'inventaire du joueur en pressant la touche "I"
     *
     * @param monster
     * @param root
     */
    private void showInventoryWindow(Entity entity, Monster monster, BorderPane root, Stage stage) {
        // stage pour la fenêtre de l'inventaire
        Stage inventoryStage = new Stage();
        inventoryStage.setTitle("Inventory");
        inventoryStage.setWidth(400);
        inventoryStage.setHeight(400);

        // GridPane pour les items de l'inventaire
        GridPane inventoryGridPane = new GridPane();
        inventoryGridPane.setHgap(10);
        inventoryGridPane.setVgap(10);

        Scene inventoryScene = new Scene(inventoryGridPane);

        if (entity instanceof Player) {
            // Crée les labels pour le nom, l'argent et la vie du joueur
            Label nameLabel = new Label("Name : " + player.getName());
            Label moneyLabel = new Label("Money : " + player.getMoney());
            Label lifeLabel = new Label("Life : " + player.getHealthPoints());

            // Ajoute les labels au GridPane
            inventoryGridPane.add(nameLabel, 0, 0);
            inventoryGridPane.add(moneyLabel, 1, 0);
            inventoryGridPane.add(lifeLabel, 2, 0);

            // Méthode pour mettre à jour les labels
            Runnable updateLabels = () -> {
                nameLabel.setText("Name : " + player.getName());
                moneyLabel.setText("Money : " + player.getMoney());
                lifeLabel.setText("Life : " + player.getHealthPoints());
            };

            int rowIndex = 1;
            for (Item item : player.getInventory()) {
                /*System.out.println("Inventory bag : ");
                System.out.println("Item : " + item.getItemName());
                System.out.println("Q : " + item.getQuantity());*/

                // Crée une image pour l'item de l'inventaire
                ImageView itemImageView = item.getImage();
                itemImageView.setFitWidth(TILE_SIZE);
                itemImageView.setFitHeight(TILE_SIZE);
                itemImageView.setLayoutX(item.getX() * TILE_SIZE);
                itemImageView.setLayoutY(item.getY() * TILE_SIZE);

                // Crée un label pour le nom de l'item et un label pour la quantité de l'item
                Label potionNameLabel = new Label(item.getItemName());
                Label quantityLabel = new Label("Quantity : " + item.getQuantity());

                // Crée un bouton pour utiliser l'item
                if (item.getEffet() != Effet.SHOWINVENTORY && item.getEffet() != Effet.ABSORB && item.getEffet() != Effet.ROB) {
                    Button useButton = new Button("Use");
                    useButton.setOnAction(e -> {
                        item.useItem(player, monster);


                        // Met à jour l'image du joueur sur la carte
                        player.getImage().setLayoutX(player.getX() * TILE_SIZE);
                        player.getImage().setLayoutY(player.getY() * TILE_SIZE);

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

                        // si le joueur utilise l'item qui le fait gagner
                        finishGame(root, stage, inventoryStage);
                    });

                    // Ajoute le bouton "Use", le label du nom de l'item, le label de la quantité de l'item et l'image de l'item au GridPane
                    inventoryGridPane.add(useButton, 0, rowIndex);
                    inventoryGridPane.add(potionNameLabel, 1, rowIndex);
                    inventoryGridPane.add(quantityLabel, 2, rowIndex);
                    inventoryGridPane.add(itemImageView, 3, rowIndex);
                }
                else {
                    inventoryGridPane.add(potionNameLabel, 1, rowIndex);
                    inventoryGridPane.add(quantityLabel, 2, rowIndex);
                    inventoryGridPane.add(itemImageView, 3, rowIndex);
                }



                rowIndex++;
            }
            inventoryStage.setScene(inventoryScene);
            inventoryStage.show();
        }

        if (entity instanceof NPC) {
            // Crée les labels pour le nom, l'argent et la vie du joueur
            Label nameLabel = new Label("Name : " + ((NPC) entity).getName());

            // Ajoute les labels au GridPane
            inventoryGridPane.add(nameLabel, 0, 0);


            // Méthode pour mettre à jour les labels
            Runnable updateLabels = () -> {
                nameLabel.setText("Name : " + ((NPC) entity).getName());
            };

            int rowIndex = 1;
            for (Item item : ((NPC) entity).getInventory()) {

                // Crée une image pour l'item de l'inventaire
                ImageView itemImageView = item.getImage();
                itemImageView.setFitWidth(TILE_SIZE);
                itemImageView.setFitHeight(TILE_SIZE);
                itemImageView.setLayoutX(item.getX() * TILE_SIZE);
                itemImageView.setLayoutY(item.getY() * TILE_SIZE);

                // Crée un label pour le nom de l'item et un label pour la quantité de l'item
                Label potionNameLabel = new Label(item.getItemName());
                Label quantityLabel = new Label("Quantity : " + item.getQuantity());


                Button useButton = new Button("Use");
                useButton.setOnAction(e -> {
                    item.useItem(player, monster);


                    // Met à jour l'image du joueur sur la carte
                    player.getImage().setLayoutX(player.getX() * TILE_SIZE);
                    player.getImage().setLayoutY(player.getY() * TILE_SIZE);

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

                    // si le joueur utilise l'item qui le fait gagner
                    finishGame(root, stage, inventoryStage);
                });


                // Ajoute le bouton "Use", le label du nom de l'item, le label de la quantité de l'item et l'image de l'item au GridPane
                inventoryGridPane.add(potionNameLabel, 1, rowIndex);
                inventoryGridPane.add(quantityLabel, 2, rowIndex);
                inventoryGridPane.add(itemImageView, 3, rowIndex);
                rowIndex++;
            }
            inventoryStage.setScene(inventoryScene);
            inventoryStage.show();
        }




    }


    /**
     * Vérifie si le joueur a ramassé un item
     *
     * @param root
     * @param tileImages
     * @param filePath
     * @param primaryStage
     */
    private void checkForItemPickup(BorderPane root, Image[][] tileImages, String filePath, Stage primaryStage) {
        Item pickedUpItems = null;
        isInInventory = false;
        for (Item item : items) {
            if (player.getX() == item.getX() && player.getY() == item.getY()) { // si le joueur est sur la même case qu'un item
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
                if (item.getItemName() == "Key") { // met à jour la porte si le joueur a ramassé la clé
                    System.out.println("You picked up a key !");
                    updateDoor(root);
                }
                if (player.getDiscoverNewWorld() == 1 && item.getEffet() == Effet.NEWWORLD) { // condition sur la clé et la porte
                    System.out.println("You finish first world !");
                    root.getChildren().removeAll();
                    primaryStage.close();
                    player.showAlert("Congratulations !", null, "You finish first world !");
                    createNewWorld(primaryStage, root);
                }
                if (player.getDiscoverNewWorld() == 0 && item.getEffet() == Effet.NEWWORLD) { // invite l'utilisateur à aller chercher la clé pour ouvrir la porte
                    System.out.println("You need a key to open the door");
                    item.showAlert("Information Dialog", null, "You need a key to open the door ! (Your player is behind the door.");
                }
                if (item.getEffet() == Effet.DEATH) { // si le joueur ramasse un item qui le tue
                    System.out.println("You are dead !");
                    primaryStage.close();
                }
            }
        }
        if (pickedUpItems != null && pickedUpItems.getEffet() != Effet.NEWWORLD) { // si l'item est ramassé, il est supprimé de la liste d'inventaire du monde
            items.remove(pickedUpItems);
            root.getChildren().remove(pickedUpItems.getImage());
        }
        updateInventoryBox(); // mise à jour de l'inventaire du joueur dans le panneau à droite de la carte
        updateCollisionMap(tileImages, filePath); // Mettre à jour la carte des collisions
    }


    /**
     * Met à jour l'image de la porte si le joueur a ramassé la clé
     *
     * @param root
     */
    private void updateDoor(BorderPane root) {
        for (Item item1 : items) {
            if ((player.getDiscoverNewWorld() == 1) && "Door".equals(item1.getItemName())) {//("Door".equals(item1.getItemName())) {
                root.getChildren().remove(item1.getImage()); // Remove the old image from the scene
                ImageView newImage = new ImageView("file:resources/Object/door.png"); // Create a new ImageView
                newImage.setFitWidth(TILE_SIZE);
                newImage.setFitHeight(TILE_SIZE);
                newImage.setLayoutX(item1.getX() * TILE_SIZE); // Set the x-coordinate of the ImageView
                newImage.setLayoutY(item1.getY() * TILE_SIZE); // Set the y-coordinate of the ImageView
                item1.setImage(newImage); // Update the image of the item
                root.getChildren().add(item1.getImage()); // Add the new image to the scene
            }
        }
    }


    /**
     * Vérifie si le joueur rencontre un pnj (type quest, villageois ou spécial) et les actions par la suite
     *
     * @param root
     * @param playerImageView
     * @param primaryStage
     */
    private void checkForNPCEncounter(BorderPane root, ImageView playerImageView, Stage primaryStage) {
        Item robItem = null;
        for (Entity entity : entities) {
            if (entity instanceof NPC) {
                NPC npc = (NPC) entity;
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
                        npc.giveItem(player, new Item(7,5,"Sword", "Monster are no more a problem.", Effet.ATTAQUEPLUS,1,"resources/Object/sword_normal.png"));
                        player.showAlert("Gift received", null, "You received a gift from "+npc.getName()+" !");
                    } /*else if (npc.getNPCType().equals(NPCType.LISTITEM)) { // item 1
                        System.out.println("List item NPC encountered");
                        //npc.TypeNPC(npc, itemsNPC);
                        if (npc.getInventory().isEmpty()) {
                            npc.showAlert("No items", null, "There are no items to show you.");
                        }
                        else {
                            showInventoryWindow(npc, null, root, primaryStage);
                        }
                    }*/
                    for (Item item : player.getInventory()) {
                        System.out.println("Première boucle");
                        if (item.getEffet().equals(Effet.SHOWINVENTORY) && npc.isRencontrePlayer()/* && item.getEffet().equals(Effet.ROB)*/) {
                            System.out.println("if show inventory");
                            for (Item item2 : player.getInventory()) {
                                System.out.println("boucle 2");
                                if (item2.getEffet().equals(Effet.ROB)) {
                                    System.out.println("item rob");
                                    if (npc.getInventory().isEmpty()) {
                                        npc.showAlert("No items", null, "There are no items to show you.");
                                        robItem = item2;
                                    }
                                    else {
                                        System.out.println("affiche inventaire pnj");
                                        showInventoryWindow(npc, null, root, primaryStage);
                                    }
                                }
                            }
                        }
                    }
                    if (robItem != null && robItem.getEffet() == Effet.ROB) { // si l'item est ramassé, il est supprimé de la liste d'inventaire du monde
                        player.getInventory().remove(robItem);
                        root.getChildren().remove(robItem.getImage());
                    }
                    npc.setRencontrePlayer(true);
                }
            }
        }

    }


    /**
     * définit les actions à effectuer pour la première quête
     *
     * @param root
     */
    private void firstQuest(BorderPane root) {
        if (npc.isFirstQuestAccepted()) { // si le joueur a accepté la quête
            if (monsterKilled) { // si le joueur a tué le monstre
                System.out.println("Quest completed");
                npc.showAlert("Quest completed", null, "You completed the quest! Pick up the key and go to the next world.");
                Item seaPotion = new Item(7,5,"SwimPotion", "water is no more a problem", Effet.SWIM,1,"resources/Object/potion_blue.png");
                items.add(seaPotion);

                // afficher l'item récompense sur la carte
                ImageView seaPotionImageView = seaPotion.getImage();
                seaPotionImageView.setFitWidth(TILE_SIZE);
                seaPotionImageView.setFitHeight(TILE_SIZE);
                root.getChildren().add(seaPotionImageView);
                seaPotionImageView.setLayoutX(seaPotion.getX() * TILE_SIZE);
                seaPotionImageView.setLayoutY(seaPotion.getY() * TILE_SIZE);

                // supprime le pnj
                //root.getChildren().remove(npc.getImage());
                System.out.println("FirstQuest : true");
                monsterKilled = false;
                isFirstQuestSucceded = true;
                return;
            }
            if (!monsterKilled && !isFirstQuestSucceded) { // si le joueur n'a pas tué le monstre et revient quand même vers le pnj
                npc.showAlert("Quest not completed", null, "You have to kill the Monster first.");
                return;
            }
            npc.showAlert("Quest completed", null, "You completed the quest! Pick up the key and go to the next world.");
        }
        else { // si le joueur n'a pas accepté la quête la premnière fois
            npc.showQuestDialog(player);
            return;
        }
    }


    /**
     * Vérifie si le joueur rencontre un monstre et les actions à effectuer
     *
     * @param root
     * @param monster
     * @param stage
     */
    private void checkForMonsterEncounter(BorderPane root, Monster monster, Stage stage) {
        if (entities.contains(monster) && player.equals(monster)) { //if (player.getX() == monster.getX() && player.getY() == monster.getY()) {
            System.out.println("Monster encountered");
            if (monster.getMonsterType().equals(MonsterType.ORC)) { // si le joueur rencontre un orc
                System.out.println("Orc Monster encountered");
                if (!npc.isFirstQuestAccepted() && !isFirstQuestSucceded) {
                    orc.showAlert("Monster encountered", null, "You have to accept the quest first.");
                    return;
                } else if (npc.isFirstQuestAccepted() && !monsterKilled) {
                    showMonsterDialog(root, monster, stage);
                }
            } else if (monster.getMonsterType().equals(MonsterType.SKELETON)) { // si le joueur rencontre un squelette (BOSS)
                System.out.println("Skeleton Monster encountered");
                showMonsterDialog(root, monster, stage);
                if (monster.getHealthPoints() <= 0) {
                    monsterKilled = true;
                    monster.showAlert("Monster defeated", null, "You defeated the boss !");
                    player.showAlert("Congratulations !", null, "You finished the game !");
                    root.getChildren().remove(monster.getImage());
                    stage.close();
                }
            }
        }
        for (Monster monster1 : monsters) { // si le joueur rencontre un autre type de monstre
            if (monster1.getMonsterType().equals(MonsterType.BAT) && player.equals(monster1)) {
                System.out.println("Bat Monster encountered");
                monster1.robItem(monster1, player); // vol alétoirement un item si l'item se trouve dans l'inventaire du joueur (l'item choisit est aléatoire)
            }
        }
    }


    /**
     * Affiche la fenêtre de dialogue avec le monstre
     *
     * @param root
     * @param monster
     * @param stage
     */
    private void showMonsterDialog(BorderPane root, Monster monster, Stage stage) {
        // Crée le dialogue avec le monstre
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Combat with Monster");
        dialog.setHeaderText("Hi "+player.getName()+", prepare to die, I will defeat you ! What will you do ?");

        // Ajoute les boutons à la fenêtre de dialogue
        ButtonType attackButtonType = new ButtonType("Attack", ButtonBar.ButtonData.YES);
        ButtonType runButtonType = new ButtonType("Run", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(attackButtonType, runButtonType);

        // Récupère les actions de la fenêtre de dialogue
        dialog.setResultConverter(buttonType -> {
            if (buttonType == attackButtonType) {
                // Le joueur attaque le monstre
                player.attack(monster);
                updateHealthPointsLabel(root, monster);
                if (monster.getHealthPoints() <= 0) { // traitement si le monstre meurt
                    monsterKilled = true;
                    monster.showAlert("Monster defeated", null, "You defeated the monster!");
                    root.getChildren().remove(monster.getImage());
                    entities.remove(monster);
                    return "Defeated";
                }
                if (player.getHealthPoints() <= 0) { // traitement si le personnage meurt
                    player.setDestoyed(true);
                    player.showAlert("Game Over", null, "The skeleton killed you !");
                    checkDestroyedPlayer(stage, monster, root);
                    return "Defeated";
                }
                System.out.println("You attacked the monster!");
                return "Attacked";
            } else if (buttonType == runButtonType) { // si le joueur fuit le monstre
                System.out.println("You ran away from the monster.");
                return "Ran";
            }
            return null;
        });

        // Affiche la boîte de dialogue
        Optional<String> result = dialog.showAndWait();
    }


    private void finishGame(BorderPane root, Stage primaryStage, Stage inventoryStage) {
        if (player.isVictory()) {
            player.showAlert("Congratulations !", null, "You finished the game !");
            root.getChildren().removeAll();
            inventoryStage.close();
            primaryStage.close();
        }
    }


    /**
     * Crée un nouveau monde (monde 2)
     *
     * @param primaryStage
     * @param root
     */
    private void createNewWorld(Stage primaryStage, BorderPane root) {
        // Par exemple, vous pouvez appeler la méthode GenMap avec un nouveau fichier de carte
        String newWorldPath = "resources/Tiles/world2.csv";
        Stage newWorldStage = new Stage();
        newWorldStage.setTitle("PokeSmart - world2");
        initCaractersWorld2(primaryStage, root);
        GenMap(newWorldStage, newWorldPath, skeleton);
    }


    /**
     * Initialise les personnages, monstres, pnj et items du monde 2 et supprime les éléments du monde précédent
     *
     * @param primaryStage
     * @param root
     */
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
        entities.add(new Monster("BatMan", 7, 3, 0, 0, 1, MonsterType.BAT, 1, 1, 1,"resources/Monster/bat_down_2.png"));
        skeleton = new Monster("Skeleton", 10, 2, 0, 0, 400, MonsterType.SKELETON, 1, 150, 200, "resources/Monster/skeletonlord_down_1.png");
        entities.add(skeleton);
        npc = new NPC("Casper", 2,4,0,0,NPCType.SPECIAL,"resources/NPC/merchant_down_1.png");
        entities.add(npc);
        monsters.add(new Monster("BatMan", 7, 3, 0, 0, 1, MonsterType.BAT, 1, 1, 1,"resources/Monster/bat_down_2.png"));
        monsters.add(skeleton);

        items = new ArrayList<Item>();
        items.add(new Item(7,4,"HealPotion", "this can heal you", Effet.HEAL,1,"resources/Object/potion_red.png"));
        items.add(new Item(7,7,"911", "Vroum vroum", Effet.VICTORY,1,"resources/Items/911-removebg-preview.png"));
    }


    /**
     * Met à jour les points de vie du joueur et du monstre
     *
     * @param root
     * @param monster
     */
    private void updateHealthPointsLabel(BorderPane root, Monster monster) {
        if (healthPointsLabel == null) {
            healthPointsLabel = new Label();
        }
        healthPointsLabel.setText("Your HP : \n" + player.getHealthPoints());

        if (monsterHealthPointsLabel == null) {
            monsterHealthPointsLabel = new Label();
        }
        monsterHealthPointsLabel.setText("Monster HP : \n" + monster.getHealthPoints());

        if (monster.getHealthPoints() <= 0) { // supression du label du monstre si le monstre n'a plus de vie
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


    /**
     * Retourne l'image correspondant à la valeur de la tuile
     *
     * @param value
     * @return
     */
    private String getImagePathForValue(int value) {
        return switch (value) {
            case 0 -> "file:resources/Tiles/ground/wall.png";
            case 1 -> "file:resources/Tiles/ground/water.png";
            case 2 -> "file:resources/Tiles/ground/sand.png";
            case 3 -> "file:resources/Tiles/ground/earth.png";
            case 4 -> "file:resources/Tiles/ground/grass.png";
            default -> null;
        };
    }


    /**
     * Lance l'application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}



