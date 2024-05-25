package PokeSmart;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class NPC extends Entity {
    private String name;
    private NPCType NPCType;
    private boolean acceptFirstQuest = false;
    private boolean secondQuest = false;
    private boolean monsterKilled = false;

    // imagePath : src/PokeSmart/NPC/oldman_down_1.png

    public NPC(String nom, double x, double y, double vx, double vy, NPCType NPCType, String imagePath) {
        super(x, y, vx, vy, 1000, imagePath);
        this.name = nom;
        this.NPCType = NPCType;
    }


    public void showQuestDialog(Player player) {
        // Create a dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Dialogue with NPC");
        dialog.setHeaderText("Hello "+player.getName()+", I am "+this.getName()+" and I have a quest for you. Do you accept?");

        // Add buttons to the dialog
        ButtonType acceptButtonType = new ButtonType("Accept", ButtonBar.ButtonData.YES);
        ButtonType refuseButtonType = new ButtonType("Refuse", ButtonBar.ButtonData.NO);
        dialog.getDialogPane().getButtonTypes().addAll(acceptButtonType, refuseButtonType);

        // Handle the result
        dialog.setResultConverter(buttonType -> {
            if (buttonType == acceptButtonType) {
                // Handle the player accepting the quest
                this.setFirstQuestAccepted(true);
                //acceptFirstQuest = true;
                this.showAlert("Quest accepted", null, "You accepted the quest! You have to kill the Monster.");
                return "Accepted";
            } else if (buttonType == refuseButtonType) {
                // Handle the player refusing the quest
                this.showAlert("Quest refused", null, "You refused the quest.");
                System.out.println("You refused the quest.");
                return "Refused";
            }
            return null;
        });

        // Show the dialog
        Optional<String> result = dialog.showAndWait();
    }



    public void speak(Player player) {
        System.out.println("Hello " + player.getName() + " I am " + this.name);
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

    public boolean isFirstQuestAccepted() {
        return acceptFirstQuest;
    }

    public void setFirstQuestAccepted(boolean firstQuest) {
        this.acceptFirstQuest = firstQuest;
    }

    public boolean isSecondQuest() {
        return secondQuest;
    }

    public void setSecondQuest(boolean secondQuest) {
        this.secondQuest = secondQuest;
    }

    public boolean isMonsterKilled() {
        return monsterKilled;
    }

    public void setMonsterKilled(boolean monsterKilled) {
        this.monsterKilled = monsterKilled;
    }
}
