package PokeSmart;

import javafx.scene.image.Image;

public class HealPotion extends Item {
    private double regenHP = 50;
    public Image img;


    /**
     * permet de créer la potion à un endroit donné
     * la potion est inutilisée à l'origine
     *
     * @param x
     * @param y
     */
    public HealPotion(double x, double y) {
        super(x, y, false);
        this.img = new Image("file:src/PokeSmart/Object/potion_red.png");
    }

    /**
     * permet de retourner la valeur des soins possibles
     *
     * @return la valeur de "regenHP"
     */
    public double getRegenHP() {
        return regenHP;
    }

    /**
     * permet de changer la valeur de regenHP si l'entité à un bonus
     *
     * @param regenHP
     */
    public void setRegenHP(double regenHP) {
        this.regenHP = regenHP;
    }

    /**
     * permet de retourner l'image correspondante de la potion
     *
     * @return img
     */
    public Image getImg() {
        return img;
    }
}
