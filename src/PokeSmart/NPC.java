package PokeSmart;

public class NPC extends Entity {
    private String name;
    private double type; //if 0 young, if 1 villager, if 2 old, if 3 special
    private double interaction; // if 0 nothing, 1 can speak with player, 2 start a quest
    private NPCType NPCType;

    // imagePath : src/PokeSmart/NPC/oldman_down_1.png

    public NPC(String nom, double x, double y, double vx, double vy, double interaction, double type, String imagePath) {
        super(x, y, vx, vy, 1000, imagePath);
        this.name = nom;
        this.interaction = interaction;
        this.type = type;
    }

    public void speak(Player player) {
        if (this.interaction == 1) {
            System.out.println("Hello " + player.getName() + " I am " + this.name);
        }
    }

    public void startQuest(Player player) {
        if (this.interaction == 2) {
            System.out.println("Hello " + player.getName() + " I am " + this.name + " I have a quest for you");
        }
    }

    public void giveItem(Player player, Item item) {
        if (this.interaction == 2) {
            player.addItem(item);
        }
    }

    public void giveMoney(Player player, double money) {
        if (this.interaction == 2) {
            player.setMoney(player.getMoney() + money);
        }
    }

    public void TypeNPC(NPC npc) {
        switch (NPCType) {
            case QUEST -> {
                npc.setType(0);
                npc.setInteraction(2);
            }
            case VILLAGER -> {
                npc.setType(1);
                npc.setInteraction(1);
            }
            case SPECIAL -> {
                npc.setType(3);
                npc.setInteraction(2);
            }
        }
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
