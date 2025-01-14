package controller.gamemanager;

import controller.Main;
import model.enums.CellStatus;
import model.enums.FireResult;
import model.game.Cell;
import model.game.Player;
import model.items.Item;
import controller.gamemanager.shipplacement.ShipPlacement;
import controller.gamemanager.shipplacement.RandomPlacementStrategy;

import java.util.*;

import static view.EventInterface.reset;
import static view.EventInterface.yellow;

public class BotFunction {
    ShipPlacement shipPlacement = new ShipPlacement();
    public BoardController boardController;
    private final Player player;
    private final boolean hardMode;
    private char lastHitChar = '@';
    private int lastHitInt = 99;
    private int direction = -1; //-1: cant choose, 0: not set, 1: down, 2: up, 3: right, 4: left
    private int distance = 1;

    private final List<Integer> attemptedDirections;
    private final Stack<Cell> targetStack;

    private static final List<int[]> DIRECTIONS = Arrays.asList(
            new int[]{1, 0},    // Down
            new int[]{-1, 0} ,   // Up
            new int[]{0, 1},    // Right
            new int[]{0, -1}   // Left
    );

    List<int[]> tempDirections = new ArrayList<>(DIRECTIONS);

    public BotFunction(Player player, BoardController boardController) {
        this.player = player;
        this.boardController = boardController;
        this.attemptedDirections = new ArrayList<>();
        this.targetStack = new Stack<>();

        System.out.print(yellow + "Bạn có muốn thử sức với chế độ Khó không? Nếu có hãy nhập \"H\": " + reset);
        String hardChoice = Main.scanner.nextLine();
        this.hardMode = hardChoice.equalsIgnoreCase("H");
    }

    public void placeShips() {
        shipPlacement.setPlacementStrategy(new RandomPlacementStrategy());
        shipPlacement.placeShips(player.getShips(), boardController);
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public FireResult fireAt(Player opponent) {
        FireResult result;
        if (direction==-1) {
            result = searchAndFire(opponent, true);
        } else {
            result = searchAndFire(opponent, false);
        }
        return result;
    }

    private FireResult searchAndFire(Player opponent, boolean scanMode) {
        FireResult result = FireResult.MISS;
        int boardSize = player.getBoard().getSize();

        if(!scanMode) {
            result = continueDirectionFire(opponent);
            if(result == null) scanMode = true;
        }

        if (scanMode) {
            if(hardMode && !targetStack.empty()) {
                Cell upcomingCell = targetStack.pop();
                direction=0;
                while(upcomingCell.getStatus()!=CellStatus.SHIP) upcomingCell=targetStack.pop();
                if(upcomingCell.getStatus()==CellStatus.SHIP) {
                    direction=0;
                    return attemptFire(opponent, upcomingCell.getXAris(), upcomingCell.getYAris(), true);
                }
            }
            for (int intAris = 1; intAris <= boardSize; intAris++) {
                for (char charAris = (char) ('A'+(intAris%2)); charAris < 'A' + boardSize; charAris+=2) {
                    result = attemptFire(opponent, charAris, intAris, true);
                    if (result != null) {
                        if(result == FireResult.HIT) direction = 0;
                        return result;
                    }
                }
            }
            for (int intAris = 1; intAris <= boardSize; intAris++) {
                for (char charAris = 'A'; charAris < 'A' + boardSize; charAris++) {
                    Cell tempCell = opponent.getBoard().getCell(charAris, intAris);
                    if(tempCell.getStatus() == CellStatus.SHIP){
                        result = attemptFire(opponent, charAris, intAris, true);
                        return result;
                    }
                }
            }
        }

        return result;
    }

    private FireResult continueDirectionFire(Player opponent) {

        if(hardMode) {
            List<Cell> opponentShipCells = opponent.getBoard().getCell(lastHitChar, lastHitInt).getShip().getCells();
            for(Cell opponentShipCell : opponentShipCells) {
                if (opponentShipCell.getStatus() == CellStatus.SHIP) {
                    for (int[] direction : DIRECTIONS){
                        char targetChar = (char) (lastHitChar + direction[0]);
                        int targetInt = lastHitInt + direction[1];
                        if(player.getBoard().isCoordinateValid(targetChar, targetInt) && opponent.getBoard().getCell(targetChar, targetInt).getStatus() == CellStatus.SHIP
                                && opponent.getBoard().getCell(targetChar, targetInt).getShip() != opponent.getBoard().getCell(lastHitChar, lastHitInt).getShip()) {
                            targetStack.push(opponent.getBoard().getCell(targetChar, targetInt));
                        }
                    }
                    return attemptFire(opponent, opponentShipCell.getXAris(), opponentShipCell.getYAris(), false);
                }
            }
        }

        FireResult result = null;

        char targetChar;
        int targetInt;
        if (direction == 0) {
            //System.out.println("Đang xoay");
            for (int tempDirection = 0; tempDirection < 4; tempDirection++) {

                if (attemptedDirections.contains(tempDirection)) {
                    continue;
                }
                int[] currentDirection = tempDirections.get(tempDirection);
                targetChar = (char) (lastHitChar + currentDirection[0]);
                targetInt = lastHitInt + currentDirection[1];
                result = attemptFire(opponent, targetChar, targetInt, false);

                if (!player.getBoard().isCoordinateValid(targetChar, targetInt)) {
                    attemptedDirections.add(tempDirection);
                    continue;
                }

                if (result != null) {
                    if (result == FireResult.HIT) {
                        if(currentDirection[1]==1) direction = 3;
                        else if(currentDirection[1]==-1) direction = 4;
                        else if(currentDirection[0]==1) direction = 1;
                        else if(currentDirection[0]==-1) direction = 2;
                        //System.out.println(direction);
                        attemptedDirections.clear();
                    } else if (result == FireResult.MISS) {
                        attemptedDirections.add(tempDirection);
                    }
                    return result;
                } else {
                    attemptedDirections.add(tempDirection);
                }
            }
        } else {
            ++distance;
            int[] currentDirection = DIRECTIONS.get(direction-1);
            targetChar = (char) (lastHitChar + currentDirection[0]*distance);
            targetInt = lastHitInt + currentDirection[1]*distance;
            Cell tempCell = opponent.getBoard().getCell(targetChar, targetInt);
            if(!opponent.getBoard().isCoordinateValid(targetChar, targetInt)
                    || tempCell.getStatus() == CellStatus.MISS
            || tempCell.getStatus() == CellStatus.HIT) {
                attemptedDirections.add(direction);
                distance = 1;
                if(direction % 2 == 1) ++direction;
                else --direction;
                currentDirection = DIRECTIONS.get(direction-1);
                targetChar = (char) (lastHitChar + currentDirection[0]*distance);
                targetInt = lastHitInt + currentDirection[1]*distance;
            }
            result = attemptFire(opponent, targetChar, targetInt, false);
        }

        return result;
    }

    private FireResult attemptFire(Player opponent, char targetChar, int targetInt, boolean newPoint) {
        if (player.getBoard().isCoordinateValid(targetChar, targetInt)) {
            Cell target = opponent.getBoard().getCell(targetChar, targetInt);
            if (target.getStatus() != CellStatus.HIT && target.getStatus() != CellStatus.MISS) {
                FireResult result = boardController.fireAt(target);
                if (result == FireResult.HIT) {
                    if(newPoint) {
                        lastHitChar = targetChar;
                        lastHitInt = targetInt;
                        Collections.shuffle(tempDirections);
                    }
                }
                else if (result == FireResult.SUNK) {
                    resetLastHit();
                    attemptedDirections.clear();
                }
                else if(result == FireResult.MISS && !newPoint) {
                    distance = 0;
                }
                System.out.println("Bot đã bắn vào ô " + targetChar + targetInt);
                return result;
            }
        }
        return null;
    }

    private void resetLastHit() {
        lastHitChar = '@';
        lastHitInt = 99;
        direction = -1;
        distance = 1;
    }

    public void useItem(BoardController opponentBoardController) {
        int boardSize = player.getBoard().getSize();
        List<Item> items=player.getItems();
        Item bomb = items.getFirst();

        for (int intAris = 1; intAris <= boardSize; intAris++) {
            for (char charAris = (char) ('A'+(intAris%2)); charAris < 'A' + boardSize; charAris+=2) {
                if (opponentBoardController.getBoard().getCell(charAris, intAris).getStatus() == CellStatus.SHIP) {
                    bomb.activate(player, opponentBoardController, charAris, intAris);
                    items.removeFirst();
                    return;
                }
            }
        }
    }

    public boolean isHardMode() {
        return hardMode;
    }
}