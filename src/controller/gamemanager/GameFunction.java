package controller.gamemanager;

import controller.Main;
import model.game.*;
import model.enums.FireResult;
import model.enums.GameStatus;
import model.items.*;
import java.util.Random;

import static view.EventInterface.*;
import view.*;


public class GameFunction {
    public Game game;
    private Player currentTurn;
    private Player opponent;
    public PlayerFunction playerFunction1;
    public PlayerFunction playerFunction2;
    public BotFunction botFunction;
    public Leaderboard leaderboard = Leaderboard.getInstance();
    public boolean isPvE;
    public boolean itemEnabled;
    public static Random random = new Random();
    public ItemManager currentItemManager;
    public boolean isPaused;
    final int REWARD_THRESHOLD = 3;
    public static GameStateFunction gameStateFunction = GameStateFunction.getInstance();
    public static EventInterface eventInterface = EventInterface.getInstance();

    public GameFunction(Game game, PlayerFunction playerFunction1, PlayerFunction playerFunction2, BotFunction botFunction, boolean isPvE, boolean itemEnabled) {
        this.game = game;
        this.playerFunction1 = playerFunction1;
        this.playerFunction2 = playerFunction2;
        this.botFunction = botFunction;
        this.isPvE = isPvE;
        this.itemEnabled = itemEnabled;
        this.isPaused = false;
        gameStateFunction.clearTempGame();
    }

    private void runGameLoop() {
        if(game.getStatus()==GameStatus.READY) readyState();
        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            currentTurn = game.getCurrentTurn();
            opponent = (currentTurn == game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
            currentItemManager = new ItemManager(currentTurn);
            System.out.println("Lượt bắn của " + red + currentTurn.getName() + reset + " sẽ bắt đầu sau 3 giây, hãy sẵn sàng!");
            if (isPvE && currentTurn == game.getPlayer2()) {
                botTurn();
            } else {
                playerTurn();
            }
            if (opponent.getRemainingShips() == 0) {
                end();
                break;
            }
            if(isPaused){
                clearScreen(1);
                break;
            }
            game.setCurrentTurn(opponent);
        }
    }

    private void readyState() {
        clearScreen(1);
        eventInterface.start();
        playerFunction1.placeShips();
        if (isPvE)
            botFunction.placeShips();
        else {
            playerFunction2.placeShips();
        }
        game.setStatus(GameStatus.IN_PROGRESS);
        System.out.println("===== Khai màn trận chiến =====");

    }

    private void status() {
        System.out.println("\n===== Lượt của " + blue + currentTurn.getName() + reset + " =====");
        System.out.println("Số tàu còn lại của địch: " + opponent.getRemainingShips());
        System.out.println("Số lần bắn trúng tàu địch: " + opponent.getHits());
        System.out.println("Số tàu của bạn đã bị phá hủy: " + currentTurn.getSunkShips());
        System.out.println("===== Bảng của " + blue + currentTurn.getName() + reset + " và Bảng sương mù =====");
        currentTurn.getBoard().displayBoardsSideBySide(opponent.getBoard());
    }

    private void playerTurn() {
        PlayerFunction currentPlayerFunction = (currentTurn == game.getPlayer1()) ? playerFunction1 : playerFunction2;
        int consecutiveHits = 0;
        while (true) {
            clearScreen(2);
            if (itemEnabled && consecutiveHits % REWARD_THRESHOLD == 0 && consecutiveHits >= REWARD_THRESHOLD) {
                System.out.println(yellow + "Bạn đã bắn trúng liên tiếp " + REWARD_THRESHOLD + " lần! Nhận được một vật phẩm." + reset);
                currentItemManager.handleItemReward();
                consecutiveHits = 0;
            }
            status();
            int choice = 1;
            if (itemEnabled) {
                System.out.println("0. Tạm dừng");
                System.out.println("1. Tấn công");
                System.out.println("2. Sử dụng vật phẩm");
                choice = EventInterface.getValidOptionWithPrompt("Lựa chọn của bạn: ",
                        "Lựa chọn của bạn không hợp lệ. Vui lòng nhập lại");
            }
            switch (choice) {
                case 0:
                    gameStateFunction.saveAndPauseGame(currentTurn, opponent, itemEnabled);
                    isPaused = true;
                    return;
                case 1:
                    FireResult result = currentPlayerFunction.fireAt(opponent.getBoard());
                    if (result == null) {
                        gameStateFunction.saveAndPauseGame(currentTurn, opponent, itemEnabled);
                        isPaused = true;
                        return;
                    }
                    handleFireResult(result);
                    if (result == FireResult.MISS || opponent.getRemainingShips() == 0) {
                        return;
                    }
                    System.out.println("Bạn đã trúng tàu địch, bạn được bắn thêm lần nữa!");
                    ++consecutiveHits;
                    break;
                case 2:
                    if (currentTurn.getItems().isEmpty()) {
                        alert("Bạn không có vật phẩm nào.");
                        sleep(2000);
                        continue;
                    }
                    currentItemManager.listItems();
                    System.out.println(yellow + "Nếu bạn không muốn chọn các vật phẩm sau đây, bạn có thể nhập \"0\" để thoát" + reset);
                    alert("Lưu ý: Nếu bạn chọn ô ngoài bản đồ, hệ thống sẽ coi như bạn từ chối sử dụng vật phẩm");
                    System.out.print("Chọn vật phẩm để sử dụng: ");
                    int itemIndex = Integer.parseInt(Main.scanner.nextLine()) - 1;
                    BoardController opponentBoardController;
                    if (playerFunction2 != null)
                        opponentBoardController = (currentTurn == game.getPlayer1()) ? playerFunction2.boardController : playerFunction1.boardController;
                    else opponentBoardController = botFunction.getBoardController();
                    currentPlayerFunction.useItem(itemIndex, opponentBoardController);
                    break;
                default:
                    alert("Lựa chọn không hợp lệ. Vui lòng nhập lại");
            }
        }
    }

    private void botTurn() {
        int consecutiveHits = 0;
        while (true) {
            //status(currentTurn, opponent);
            clearScreen(2);
            if(botFunction.isHardMode() && itemEnabled && consecutiveHits % REWARD_THRESHOLD == 0 && consecutiveHits >= REWARD_THRESHOLD) {
                System.out.println(yellow + "Bot đã bắn trúng liên tiếp " + REWARD_THRESHOLD + " lần! Nhận được một vật phẩm." + reset);
                currentTurn.addItem(new Bomb());
                consecutiveHits=0;
            }
            System.out.println("\n===== Lượt của " + currentTurn.getName() + " =====");
            if(!currentTurn.getItems().isEmpty()) {
                botFunction.useItem(playerFunction1.boardController);
                clearScreen(2);
            }
            FireResult result = botFunction.fireAt(opponent);
            handleFireResult(result);
            if (result == FireResult.MISS || opponent.getRemainingShips() == 0) {
                break;
            }
            System.out.println(currentTurn.getName() + " đã trúng tàu của bạn, nó sẽ bắn thêm lần nữa!");

            ++consecutiveHits;
        }
    }

    private void handleFireResult(FireResult result) {
        switch (result) {
            case HIT:
                eventInterface.hit();
                break;
            case MISS:
                eventInterface.miss();
                break;
            case SUNK:
                eventInterface.sunk();
                break;
        }
        currentTurn.setTurn(currentTurn.getTurn()+1);
    }

    public void end(){
        clearScreen(1);
        game.setStatus(GameStatus.FINISHED);
        eventInterface.end();
        System.out.println(currentTurn.getName() + " chiến thắng!");
        sleep(1000);
        System.out.println("===== Bảng của " + currentTurn.getName() + " và Bảng sương mù =====");

        currentTurn.getBoard().displayBoardsSideBySide(opponent.getBoard());
        System.out.println("Màn hình chính sẽ được mở sau 5s.");
        clearScreen(5);
        leaderboard.addRecord(new PlayerRecord(currentTurn.getName(), currentTurn.getTurn(), currentTurn.getRemainingShips()));
    }

    public void startGameLoop(){
        runGameLoop();
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void clearScreen(int seconds) {
        sleep(seconds*1000);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}