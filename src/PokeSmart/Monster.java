package PokeSmart;

import java.util.Random;

public class Monster extends Entity {
    private String name;
    private MonsterType monsterType;
    private double strength;
    private double attacks;
    private double defences;

    // imagePath : src/PokeSmart/Monster/orc_down_2.png

    public Monster(String name, double x, double y, double vx, double vy, int HealthPoints, MonsterType monsterType, double strength, double attacks, double defences, String imagePath) {
        super(x, y, vx, vy, HealthPoints, imagePath);
        this.name = name;
        this.monsterType = monsterType;
        this.strength = strength;
        this.attacks = attacks;
        this.defences = defences;
    }

    public void TypeMonster(Monster monster) {
        switch (monsterType) {
            case ORC -> {
                monster.setStrength(50);
                monster.setAttacks(20);
                monster.setDefences(50);
                monster.setHealthPoints(150);
                //monster.setBehavior(1);
            }
            case SKELETON -> {
                monster.setStrength(150);
                monster.setAttacks(60);
                monster.setDefences(100);
                monster.setHealthPoints(250);
                //monster.setBehavior(2);
            }
            case BAT -> {
                monster.setStrength(100);
                monster.setAttacks(40);
                monster.setDefences(80);
                monster.setHealthPoints(200);
                //monster.setBehavior(3);
            }
        }
    }

    public void robItem(Monster monster, Player player, Item item) { // permet au monstre de voler un item du joueur selon l'effet voulu
        for (Item items : player.getInventory()) {
            if (items.getEffet().equals(item.getEffet())) {
                item.setQuantity(item.getQuantity() - 1);
                monster.addItem(item);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
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

    /*
    public double getBehavior() {
        return behavior;
    }
    public void setBehavior(double behavior) {
        this.behavior = behavior;
    }*/



    public void afficheCapacities(Monster monster) {
        //implementsBehavior(monster);
        System.out.println("Monster attack : "+monster.attacks);
        System.out.println("Monster health : "+monster.getHealthPoints());
        System.out.println("Monster type : "+monster.getMonsterType());
    }
    /*
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
    }*/
}
