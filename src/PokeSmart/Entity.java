package PokeSmart;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entity {
    private double x; // coordonnée en abscisse
    private double y; // coordonnée en ordonnée (inversée en java)
    private double vx; // vitesse en x
    private double vy; // vitesse en y
    private int healthPoints;
    private int capacities; // if = 1 => overwall, 2 overwall and swim, 3 overwall and key and swim
    private int discoverNewWorld; // if = 1 => can go to first new world
    private ImageView Image;
    private boolean destoyed = false; // permet de détruite l'individu en cas de perte de vie par exemple
    private List<Item> inventory = new ArrayList<>();

    // Capacités spéciales en fonction des items possédés
    private boolean canOverWall = false;
    private boolean canSwim = false;
    private boolean canOpenDoor = false;

    /**
     * constructeur de l'entité
     * permet de lui associer des coordonnées et une vitesse pour ces déplacements, horizontaux ou verticaux
     *
     * @param x
     * @param y
     * @param vx
     * @param vy
     *
     */
    public Entity(double x, double y, double vx, double vy, int healthPoints, int capacities, String imagePath) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.healthPoints = healthPoints;
        this.capacities = capacities;
        this.Image = new ImageView("file:"+imagePath);
        this.discoverNewWorld = 0;
    }

    /**
     * permet de savoir deux entités sont aux mêmes coordonnées
     *
     * @param obj
     * @return true ou false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            Entity p = (Entity) obj;
            if (this.x == p.getX() && this.y == p.getY()){
                return true;
            }
        }
        return false;
    }

    public boolean getDestroyed() {
        return destoyed;
    }
    public void setDestoyed(boolean destoyed) {
        this.destoyed = destoyed;
    }

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }
    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }

    public boolean isDestoyed() {
        return destoyed;
    }
    public void setDestoyed() {
        this.destoyed = true;
    }

    public double getHealthPoints() {
        return healthPoints;
    }
    public void setHealthPoints(int healthPoints) {
        /*if (healthPoints > 100) {
            this.healthPoints = 100;
        } else if (healthPoints < 0) {
            this.healthPoints = 0;
        }
        else {*/
            this.healthPoints = healthPoints;
        //}
    }

    public int getCapacities() {
        return capacities;
    }
    public void setCapacities(int capacities) {
        this.capacities = capacities;
    }

    public ImageView getImage() {
        return Image;
    }
    public void setImage(ImageView itemImage) {
        this.Image = itemImage;
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }
    public List<Item> getInventory() {
        return inventory;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public int getDiscoverNewWorld() {
        return discoverNewWorld;
    }
    public void setDiscoverNewWorld(int discoverNewWorld) {
        this.discoverNewWorld = discoverNewWorld;
    }

    public String getInventoryAsString() {
        StringBuilder inventoryString = new StringBuilder("Your inventory :\n");
        for (Item item : inventory) {
            inventoryString.append(item.getItemName()).append("\n");
        }
        return inventoryString.toString();
    }


    // capacités

    public boolean isCanOverWall() {
        return canOverWall;
    }
    public void setCanOverWall(boolean canOverWall) {
        this.canOverWall = canOverWall;
    }

    public boolean isCanSwim() {
        return canSwim;
    }
    public void setCanSwim(boolean canSwim) {
        this.canSwim = canSwim;
    }

    public boolean isCanOpenDoor() {
        return canOpenDoor;
    }
    public void setCanOpenDoor(boolean canOpenDoor) {
        this.canOpenDoor = canOpenDoor;
    }

    public int getItemQuantity(Item item) {
        return this.getInventory().stream().filter(i -> i.getItemName().equals(item.getItemName())).collect(Collectors.toList()).size();
    }
}
