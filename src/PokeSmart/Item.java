package PokeSmart;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item extends Entity {
    private String itemName;
    private String itemDescription;
    private Effet effet;
    private int quantity;
    private ImageView itemImage;

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
        super(x, y, 0, 0);
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.effet = effet;
        this.quantity = quantity;
        this.itemImage = new ImageView("file:"+imagePath);
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

    public ImageView getItemImage() {
        return itemImage;
    }

    public void setItemImage(ImageView itemImage) {
        this.itemImage = itemImage;
    }
}
