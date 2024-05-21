package PokeSmart;


import javafx.scene.image.Image;

public class Item extends Entity {
    private boolean status; /** permet de savoir si l'item est utilisé ou non */
    private Image image;

    /** constructeur d'item
     * permet de créer un item, lui associer des coordonnées et de savoir s'il est dans l'inventaire ou non
     *
     * @param x
     * @param y
     * @param status
     *
     */
    public Item(double x, double y, boolean status, String imagePath) {
        super(x, y, 0, 0);
        this.status = status;
        this.image = new Image("file:"+imagePath);
    }

    /**
     * retourne le statut de l'item
     *
     * @return status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * modifie le statut de l'item
     *
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
