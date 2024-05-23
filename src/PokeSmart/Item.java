package PokeSmart;

import java.util.Objects;

public class Item extends Entity {
    private String itemName;
    private String itemDescription;
    private Effet effet;
    private int quantity;

    /**
     * constructeur d'item
     *
     * @param x
     * @param y
     * @param itemName
     * @param itemDescription
     * @param effet
     * @param quantity
     * @param imagePath
     */
    public Item(double x, double y, String itemName, String itemDescription, Effet effet, int quantity, String imagePath) {
        super(x, y, 0, 0,0, 0, imagePath);
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.effet = effet;
        this.quantity = quantity;
    }

    public void useItem(Entity entity){
        switch (effet){
            case HEAL -> entity.setHealthPoints((int) (entity.getHealthPoints()+20));
            case OVERWALL -> {
                entity.setCanOverWall(true);
            }
            case SWIM -> {
                entity.setCanSwim(true);
            }
            case OPENDOOR -> {
                entity.setDiscoverNewWorld(1);
            }
            case NEWWORLD -> { // à changer
                //entity.setX(0);
                //entity.setY(0);
            }
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Effet getEffet() {
        return effet;
    }

    public void setEffet(Effet effet) {
        this.effet = effet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
