package PokeSmart;

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
            case HEAL -> entity.setHealthPoints(entity.getHealthPoints()+20);
            case OVERWALL -> {
                if (entity.getCapacities() == 0) { entity.setCapacities(1);} // si rien alors juste wall
                else if(entity.getCapacities() == 2) { entity.setCapacities(3);} // si swim alors wall et swim
                else if(entity.getCapacities() == 4) { entity.setCapacities(5);} // si clé alors clé et wall
                else if(entity.getCapacities() == 6) { entity.setCapacities(7);} // si clé et swim alors clé et wall et swim
            }
            case SWIM -> {
                if (entity.getCapacities() == 0) { entity.setCapacities(2);} // si rien alors juste swim
                else if(entity.getCapacities() == 1) { entity.setCapacities(3);} // si wall alors wall et swim
                else if(entity.getCapacities() == 4) { entity.setCapacities(6);} // si clé alors clé et swim
                else if(entity.getCapacities() == 5) { entity.setCapacities(7);} // si clé et wall alors clé et wall et swim
            }
            case OPENDOOR -> {
                if (entity.getCapacities() == 0) { entity.setCapacities(4);} // si rien alors clé
                else if(entity.getCapacities() == 1) { entity.setCapacities(5);} // si wall alors clé et wall
                else if(entity.getCapacities() == 2) { entity.setCapacities(6);} // si swimc alors lé et swim
                else if(entity.getCapacities() == 3) { entity.setCapacities(7);} // si wall et swim alors clé et wall et swim
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
