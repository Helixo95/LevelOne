package PokeSmart;

import java.util.Random;

public class Monster extends Entity {
    private String name;
    private String type;
    private double strength;
    private double attacks;
    private double defences;
    private double behavior; // if 0 harmless, 1 is low attack, 2 may be like a normal player, 3 is offensive, 4 is totally random

    // imagePath : src/PokeSmart/Monster/orc_down_2.png

    public Monster(String name, double x, double y, double vx, double vy, int HealthPoints, int capacities, String type, double strength, double attacks, double defences, double behavior, String imagePath) {
        super(x, y, vx, vy, HealthPoints, capacities, imagePath);
        this.name = name;
        this.type = type;
        this.strength = strength;
        this.attacks = attacks;
        this.defences = defences;
        this.behavior = behavior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public double getStrength() {
        return strength;
    }
    public void setStrength(double strengh) {
        this.strength = strengh;
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

    public double getBehavior() {
        return behavior;
    }
    public void setBehavior(double behavior) {
        this.behavior = behavior;
    }



    public void afficheCapacities(Monster monster) {
        implementsBehavior(monster);
        System.out.println("Monster attack : "+monster.attacks);
        System.out.println("Monster health : "+monster.getHealthPoints());
        System.out.println("Monster type : "+monster.type);
    }
    private void implementsBehavior(Monster monster) { //set attack in terms of behavior
        if (monster.behavior == 0){
            monster.setAttacks(0);
        }
        else if (monster.behavior == 1){
            Random r = new Random();
            int valeur = r.nextInt(10-0);
            monster.setAttacks(valeur);
        }
        else if (monster.behavior == 2){
            Random r = new Random();
            int valeur = r.nextInt(20-10) + 10;
            monster.setAttacks(valeur);
        }
        else if (monster.behavior == 3){
            Random r = new Random();
            int valeur = r.nextInt(40-20) + 20;
            monster.setAttacks(valeur);
        }
        else if (monster.behavior == 4){
            Random r = new Random();
            int valeur = r.nextInt(100);
            monster.setDefences(valeur);
        }
        else {
            monster.setAttacks(10);
        }
    }
}
