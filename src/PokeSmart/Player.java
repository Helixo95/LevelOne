package PokeSmart;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private String name;
    private double healthPoints;
    private double strength;
    private double attacks; // 0 basics, 1 or 2 other attacks
    private double defences;
    private double money;
    private double type; //if 0 young, if 1 villager, if 2 old, if 3 special
    private Image playerImage;


    public Player(String nom, double x, double y, double vx, double vy, double healthPoints, double strength, double attacks, double defences, double money, double type) {
        super(x, y, vx, vy);
        this.name = nom;
        this.healthPoints = healthPoints;
        this.strength = strength;
        this.attacks = attacks;
        this.defences = defences;
        this.money = money;
        this.type = type;
        this.playerImage = new Image("file:src/PokeSmart/Player/Walking sprites/boy_down_1.png");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = healthPoints;
    }

    public double getStrength() {
        return strength;
    }
    public void setStrength(double strength) {
        this.strength = strength;
    }

    public double getAttacks() {
        return attacks;
    }
    public void setAttacks(double attacks) {
        this.attacks = attacks;
    }

    public double getDefences() {
        return defences;
    }
    public void setDefences(double defences) {
        this.defences = defences;
    }

    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }

    public double getType() {
        return type;
    }
    public void setType(double type) {
        this.type = type;
    }

    public Image getPlayerImage() {
        return playerImage;
    }
    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }
}
