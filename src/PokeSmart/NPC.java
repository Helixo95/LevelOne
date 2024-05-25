package PokeSmart;

import javafx.scene.layout.BorderPane;

public class NPC extends Entity {
    private String name;
    private NPCType NPCType;

    // imagePath : src/PokeSmart/NPC/oldman_down_1.png

    public NPC(String nom, double x, double y, double vx, double vy, NPCType NPCType, String imagePath) {
        super(x, y, vx, vy, 1000, imagePath);
        this.name = nom;
        this.NPCType = NPCType;
    }

    public void speak(Player player) {
        System.out.println("Hello " + player.getName() + " I am " + this.name);
    }

    public void startQuest(Player player, BorderPane borderPane) {
        System.out.println("Hello " + player.getName() + " I am " + this.name + " I have a quest for you");
        showAlert("Quest", null, "Hello " + player.getName() + " I am " + this.name + " I have a quest for you");

    }

    public void giveItem(Player player, Item item) {
        player.addItem(item);
    }

    public void giveMoney(Player player, double money) {
        player.setMoney(player.getMoney() + money);
    }

    public void takeMoney(Player player, double money) {
        player.setMoney(player.getMoney() - money);
    }

    public void TypeNPC(NPC npc) {
        switch (NPCType) {
            case QUEST -> {
            }
            case VILLAGER -> {
            }
            case SPECIAL -> {
            }
        }
    }

    public PokeSmart.NPCType getNPCType() {
        return NPCType;
    }

    public void setNPCType(PokeSmart.NPCType NPCType) {
        this.NPCType = NPCType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
