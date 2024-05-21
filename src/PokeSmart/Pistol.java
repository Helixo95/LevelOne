package PokeSmart;

import javafx.scene.image.Image;

public class Pistol extends Item {
    private double damages;
    public static Image img = new Image("file:Items/Pistol.PNG");

    /**
     * prend en entrée les coordonées de l'objet
     * initialise le statut à "false" car le pistolet n'est pas utilisé au début, il faut le trouver
     *
     * @param x coordonée de l'abscisse du pistolet
     * @param y coordonée de l'ordonnée du pistolet
     */
    public Pistol(double x, double y) {
        super(x, y, false);
        damages = 45;
    }

    /**
     * permet de retourner l'attaque du pistolet
     *
     * @return les dommages causés par le pistolet
     */
    public double getDamages() {
        return damages;
    }

    /**
     * prend en compte un changement de dégâts en fonction d'un possible bonus
     *
     * @param damages
     */
    public void setDamages(double damages) {
        this.damages = damages;
    }

    /**
     * renvoie l'image correspondante au pistolet
     *
     * @return l'image du pistolet
     */
    public static Image getImg() {
        return img;
    }

    public static void setImg(Image img) {
        Pistol.img = img;
    }
}
