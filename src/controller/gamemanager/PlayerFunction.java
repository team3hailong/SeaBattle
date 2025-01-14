package controller.gamemanager;

import controller.Main;
import controller.gamemanager.shipplacement.*;
import model.game.*;
import model.enums.*;

import model.items.Item;

import java.util.List;

import static view.EventInterface.*;

public class PlayerFunction {
    public ShipPlacement shipPlacement = new ShipPlacement();
    public BoardController boardController;
    private final Player player;

    public PlayerFunction(Player player, BoardController boardController) {
        this.player = player;
        this.boardController = boardController;
    }

    public void placeShips() {
        System.out.println("Lượt đặt thuyền của " + blue + player.getName() + reset + " sẽ bắt đầu sau 3s");
        GameFunction.clearScreen(2);
        int option = view.EventInterface.getValidOptionWithPrompt("Nếu bạn muốn hệ thống tự động đặt thuyền, hãy nhập \"0\", còn không thì bạn hãy gõ bất kì số khác: ",
                "Bạn đã chọn cách đặt không nằm trong hệ thống, vui lòng nhập lại: ");
        if(option == 0) {
            shipPlacement.setPlacementStrategy(new RandomPlacementStrategy());
        }
        else {
            shipPlacement.setPlacementStrategy(new ManualPlacementStrategy());
        }
        shipPlacement.placeShips(player.getShips(), boardController);
    }

    public FireResult fireAt(Board opponentBoard) {
        FireResult result;
        char xAris;
        int yAris;
        while(true) {
            System.out.println(yellow + "⚠ Nếu bạn muốn tạm dừng, hãy nhập tọa độ là \"00\"" + reset);
            System.out.print(player.getName() + ", hãy nhập tọa độ bắn (ví dụ: B5): ");
            String input = Main.scanner.nextLine().toUpperCase();
            if (input.equals("00")) {
                return null;
            }
            if(opponentBoard.isNotValidCoordinateFormat(input)) {
                alert("Định dạng tọa độ không hợp lệ. Vui lòng thử lại.");
                continue;
            }
            xAris = input.charAt(0);
            yAris = Integer.parseInt(input.substring(1));
            if(opponentBoard.isCoordinateValid(xAris, yAris)) {
                Cell target = opponentBoard.getCell(xAris, yAris);
                if (target.getStatus() != CellStatus.HIT && target.getStatus() != CellStatus.MISS) {
                    result = boardController.fireAt(target);
                    break;
                } else {
                    alert("Bạn đã bắn điểm này trước đó.");
                }
            }
            else{
                alert("Bạn đã nhập ô ngoài bản đồ. Vui lòng nhập lại...");
            }
        }
        return result;
    }

    public void useItem(int index, BoardController opponentBoardController) {
        List<Item> items = player.getItems();
        if (index >= 0 && index < items.size()) {
            Item item = items.get(index);
            System.out.print("Bạn hãy nhập tọa độ kích hoạt vật phẩm (ví dụ: B5): ");
            String input = Main.scanner.nextLine().toUpperCase();
            try {
                char x = input.charAt(0);
                int y = Integer.parseInt(input.substring(1));
                if (opponentBoardController.getBoard().isCoordinateValid(x, y)) {
                    item.activate(player, opponentBoardController, x, y);
                }
            } catch (Exception e) {
                alert("Định dạng tọa độ không hợp lệ. Vui lòng thử lại.");
            }
            items.remove(index);
        } else {
            System.out.println("Chuyển sang giao diện xem bản đồ...");
        }
    }
}