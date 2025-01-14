package controller.gamemanager.shipplacement;

import controller.Main;
import controller.gamemanager.BoardController;
import model.enums.Orientation;
import model.game.*;

import java.util.List;
import java.util.Scanner;

import static view.EventInterface.alert;

public class ManualPlacementStrategy implements PlacementStrategy {
    private final Scanner scanner = Main.scanner;

    @Override
    public void placeShips(List<Ship> ships, BoardController boardController) {
        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                try {
                    System.out.printf("Bạn đang đặt vị trí cho tàu %s (Kích thước %dx1):\n", ship.getType(), ship.getSize());
                    Cell startCell = getStartCell(boardController);
                    Orientation orientation = getOrientation();

                    placed = boardController.placeShip(ship, startCell, orientation);
                    if (placed) {
                        displayBoard(boardController);
                    } else {
                        alert("Tàu bị đặt ở vị trí không hợp lệ, vui lòng đặt lại.");
                    }
                } catch (Exception e) {
                    alert("Toạ độ hoặc hướng không hợp lệ, vui lòng nhập lại.");
                }
            }
        }
    }

    private Cell getStartCell(BoardController boardController) throws IllegalArgumentException {
        while (true) {
            System.out.print("Vui lòng nhập toạ độ góc trên bên trái của tàu (Ví dụ: A1)): ");
            String coordinate = scanner.nextLine().trim();
            if (boardController.getBoard().isNotValidCoordinateFormat(coordinate)) {
                alert("Toạ độ không hợp lệ. Vui lòng nhập lại (ví dụ: A1).");
                continue;
            }
            char xAxis = coordinate.charAt(0);
            int yAxis = Integer.parseInt(coordinate.substring(1));
            Cell startCell = boardController.getBoard().getCell(xAxis, yAxis);
            if (startCell != null) {
                return startCell;
            } else {
                alert("Bạn đã nhập ô ngoài bản đồ. Vui lòng nhập lại...");
            }
        }
    }

    private Orientation getOrientation() throws IllegalArgumentException {
        while (true) {
            System.out.print("Vui lòng chọn hướng tàu (H: Đặt ngang, V: Đặt dọc): ");
            String orientationInput = scanner.nextLine().trim().toUpperCase();

            switch (orientationInput) {
                case "H":
                    return Orientation.HORIZONTAL;
                case "V":
                    return Orientation.VERTICAL;
                default:
                    alert("Hướng bạn chọn không hợp lệ, vui lòng chọn lại (H hoặc V).");
            }
        }
    }

    private void displayBoard(BoardController boardController) {
        Board board = boardController.getBoard();
        board.displayBoardsSideBySide(board);
    }
}
