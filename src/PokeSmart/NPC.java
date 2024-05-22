package PokeSmart;

public class NPC extends Entity {
    private String name;
    private double type; //if 0 young, if 1 villager, if 2 old, if 3 special
    private double interaction; // if 0 nothing, 1 can speak with player, 2 start a quest

    // imagePath : src/PokeSmart/NPC/oldman_down_1.png

    public NPC(String nom, double x, double y, double vx, double vy, double interaction, double type, int capacities, String imagePath) {
        super(x, y, vx, vy, 1000, capacities, imagePath);
        this.name = nom;
        this.interaction = interaction;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getType() {
        return type;
    }
    public void setType(double type) {
        this.type = type;
    }

    public double getInteraction() {
        return interaction;
    }
    public void setInteraction(double interaction) {
        this.interaction = interaction;
    }
}
