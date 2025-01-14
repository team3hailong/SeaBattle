package controller.gamemanager;

import model.game.Player;
import model.items.*;
import static controller.gamemanager.GameFunction.*;

public class ItemManager {
    private final Player currentTurn;

    public ItemManager(Player player) {
        this.currentTurn = player;
    }

    public void handleItemReward() {
        int randomItem = random.nextInt(3);
        System.out.println("Bạn có thể mở túi đồ để sử dụng vật phẩm.");
        sleep(2000);

        switch (randomItem) {
            case 1:
                currentTurn.addItem(new Bomb());
                break;
            case 2:
                currentTurn.addItem(new Light());
                break;
            default:
                currentTurn.addItem(new Shield());
                break;
        }
    }

    public void listItems() {
        System.out.println("Danh sách vật phẩm:");
        for (int itemIndex = 0; itemIndex < currentTurn.getItems().size(); itemIndex++) {
            System.out.println((itemIndex + 1) + ". " + currentTurn.getItems().get(itemIndex).getName());
        }
    }
}