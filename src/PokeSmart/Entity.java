package PokeSmart;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private double x; // coordonnée en abscisse
    private double y; // coordonnée en ordonnée (inversée en java)
    private double vx; // vitesse en x
    private double vy; // vitesse en y
    private double healthPoints;
    private int capacities; // if = 1 => overwall, 2 overwall and swim, 3 overwall and key and swim
    private ImageView Image;
    private boolean destoyed = false; // permet de détruite l'individu en cas de perte de vie par exemple
    private List<Item> inventory = new ArrayList<>();

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
    public Entity(double x, double y, double vx, double vy, double healthPoints, int capacities, String imagePath) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.healthPoints = healthPoints;
        this.capacities = capacities;
        this.Image = new ImageView("file:"+imagePath);
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

    // retourne l'abscisse de l'entité, ne prend rien en paramètres
    public double getX() {
        return x;
    }

    /**
     * permet de modifier l'abscisse de l'entité
     * prend en paramètre la nouvelle abscisse
     */
    public void setX(double x) {
        this.x = x;
    }

    // retourne l'ordonnée de l'entité, ne prend rien en paramètres
    public double getY() {
        return y;
    }

    /**
     *  permet de modifier l'ordonnée de l'entité
     * prend en paramètre la nouvelle ordonnée
     */
    public void setY(double y) {
        this.y = y;
    }

    // retourne la vitesse horizontale de l'entité
    public double getVx() {
        return vx;
    }

    /**
     *  modifie vitesse horizontale de l'entité
     * prend en paramètre la nouvelle vitesse horizontale
     */
    public void setVx(double vx) {
        this.vx = vx;
    }

    // retourne la vitesse verticale de l'entité
    public double getVy() {
        return vy;
    }

    /**
     * modifie vitesse verticale de l'entité
     * prend en paramètre la nouvelle vitesse verticale
     */
    public void setVy(double vy) {
        this.vy = vy;
    }

    // retourne "true "si l'entité est détruite ou "false" si l'entité est vivante
    public boolean isDestoyed() {
        return destoyed;
    }

    /**
     * modifie l'état de l'entité
     * détruit l'entité si on lui passe "True" et l'empêche de mourir si on passe "False"
     * utile lors de l'usage de potions par exemple
     */
    public void setDestoyed() {
        this.destoyed = true;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(double healthPoints) {
        if (healthPoints > 100) {
            this.healthPoints = 100;
        } else if (healthPoints < 0) {
            this.healthPoints = 0;
        }
        else {
            this.healthPoints = healthPoints;
        }
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
        inventory.add(item);
    }
    public List<Item> getInventory() {
        return inventory;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    public String getInventoryAsString() {
        StringBuilder inventoryString = new StringBuilder("Your inventory :\n");
        for (Item item : inventory) {
            inventoryString.append(item.getItemName()).append("\n");
        }
        return inventoryString.toString();
    }

}
