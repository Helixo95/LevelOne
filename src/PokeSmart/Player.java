package PokeSmart;

public class Player extends Entity {
    private String name;
    private double strength;
    private double attacks; // 0 basics, 1 or 2 other attacks
    private double defences;
    private double money;
    private double type; //if 0 young, if 1 villager, if 2 old, if 3 special

    public Player(String nom, double x, double y, double vx, double vy, double healthPoints, double strength, double attacks, double defences, double money, double type, String imagePath) {
        super(x, y, vx, vy, healthPoints, imagePath);
        this.name = nom;
        this.strength = strength;
        this.attacks = attacks;
        this.defences = defences;
        this.money = money;
        this.type = type;
    }

    public void attack(Entity entity) {
        if (entity instanceof Monster) {
            Monster monster = (Monster) entity;
            //if (monster.getDefences() < this.strength) {
            while (monster.getHealthPoints() > 0 && this.getHealthPoints() > 0) {
                monster.setHealthPoints((int) (monster.getHealthPoints() - this.attacks));
                this.setHealthPoints((int) (this.getHealthPoints() - monster.getAttacks()));
                System.out.println("Player attacks Monster");
                for (Item item : this.getInventory()) {
                    if (item.getEffet() == Effet.ABSORB) {
                        item.useItem(this, monster);
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
