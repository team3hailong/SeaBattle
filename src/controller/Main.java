package controller;

import java.io.File;
import java.util.Scanner;
import controller.gamemanager.*;
import model.game.*;
import view.MenuInterface;

import static view.EventInterface.*;


public class Main {
    private static final String SAVE_FILE = "tempGame.txt";
    private static final String BOT_NAME = "BotMadeByHaiLong";
    public static final Scanner scanner = new Scanner(System.in);
    private final Leaderboard leaderboard = Leaderboard.getInstance();
    private final SoundController soundController = SoundController.getInstance();
    public static Sound sound = Sound.getInstance();
    public static GameStateFunction gameStateFunction = GameStateFunction.getInstance();
    public static MenuInterface menuInterface = MenuInterface.getInstance();

    public static void main(String[] args) {
        new Main().run();
        scanner.close();
    }

    public void run() {
        
        while (true) {
            initializeSound();
            GameFunction.clearScreen(1);
            menuInterface.seaBattle();
            int option = view.EventInterface.getValidOptionWithPrompt("Chọn một tùy chọn: ",
                    "Lựa chọn không tồn tại, vui lòng nhập lại");
            switch (option) {
                case 1:
                    handleGameOption();
                    break;
                case 2:
                    displayLeaderboard();
                    break;
                case 3:
                    sound.toggleSound();
                    break;
                case 4:
                    displayInstructions();
                    break;
                case 5:
                    continueGame();
                    break;
                case 0:
                    System.out.println("Cảm ơn bạn đã chơi trò chơi!");
                    return;
                default:
                    System.out.println();
            }
        }
    }

    private void initializeSound() {
        sound.stopAllClips();
        sound.isSoundEnabledChecker();
        soundController.playSoundWithDurationAsync("/resources/sound/ready.wav", true, 900000, 0.3f);
    }

    private void handleGameOption() {
        int option = view.EventInterface.getValidOptionWithPrompt(menuInterface.gameplayPicker(), "Lựa chọn không hợp lệ, vui lòng nhập lại: ");

        int size = getBoardSize();
        String player1Name;
        String player2Name = BOT_NAME;

        if (option == 1) {
            System.out.print("Nhập tên người chơi 1: ");
            player1Name = scanner.nextLine();
            System.out.print("Nhập tên người chơi 2: ");
            player2Name = scanner.nextLine();
        }
        else {
            System.out.print("Nhập tên của bạn: ");
            player1Name = scanner.nextLine();
        }

        Game game = new Game(player1Name, player2Name, size);
        BoardController boardController1 = new BoardController(game.getPlayer1().getBoard());
        BoardController boardController2 = new BoardController(game.getPlayer2().getBoard());
        PlayerFunction playerFunction1 = new PlayerFunction(game.getPlayer1(), boardController1);
        PlayerFunction playerFunction2 = (option == 1) ? new PlayerFunction(game.getPlayer2(), boardController2) : null;
        BotFunction botFunction = (option != 1) ? new BotFunction(game.getPlayer2(), boardController2) : null;

        GameFunction gameFunction = new GameFunction(game, playerFunction1,
                playerFunction2, botFunction,
                botFunction != null, isItemsEnabled());
        gameFunction.startGameLoop();
    }

    private int getBoardSize() {
        return view.EventInterface.getValidOptionWithPrompt("Hãy chọn kích thước bảng: ","Kích thước bảng vừa nhập không phải số nguyên, vui lòng nhập lại.");
    }

    private boolean isItemsEnabled() {
        System.out.println("+-------------------------------Danh sách vật phẩm-----------------------------------------+");
        System.out.println("| Người chơi sẽ được tặng ngẫu nhiên 1 trong 3 vật phẩm sau khi bắn trúng 3 lần liên tiếp: |");
        System.out.println("|    - Bom: Phát nổ toàn bộ khu vực lân cận ô được chọn, diện tích 2x2                     |");
        System.out.println("|    - Khiên: Bảo vệ tàu chứa ô được chọn, chỉ có tác dụng 1 lần                           |");
        System.out.println("|    - Đèn: Cho biết ô được chọn có tàu xuất hiện hay không                                |");
        System.out.println("+------------------------------------------------------------------------------------------+\n");
        System.out.print("Bạn có muốn bật chế độ vật phẩm không? Nếu có, hãy nhập \"Y\": ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("y");
    }

    private void displayLeaderboard() {
        leaderboard.display();
        System.out.println("Vui lòng nhập bất kì kí tự nào để thoát bảng xếp hạng.");
        scanner.nextLine();
    }

    private void displayInstructions() {
        menuInterface.instructions();
        System.out.println("Vui lòng nhập bất kì kí tự nào để thoát trang luật chơi.");
        scanner.nextLine();
    }

    private void continueGame() {
        File file = new File(SAVE_FILE);
        if (file.length() > 0) {
            GameState gameState = gameStateFunction.loadGame();
            if(gameState != null){
                boolean isPvE = gameState.player2().getName().equals(BOT_NAME);
                if(isPvE){
                    System.out.println("Bạn đang chơi với máy, liệu bạn có muốn tiếp tục không?");
                }
                else{
                    System.out.println("Ván chơi này là cuộc so tài giữa " + gameState.player1().getName() + " và "
                    + gameState.player2().getName() + ", 2 bạn đã sẵn sàng?");
                }
                System.out.print(yellow + "Nếu đồng ý tiếp tục, hãy gõ \"Y\": " + reset);
                String input = scanner.nextLine().trim();
                if(!input.equalsIgnoreCase("y")){
                    return;
                }
                PlayerFunction playerFunction1 = new PlayerFunction(gameState.player1(), new BoardController(gameState.player1().getBoard()));

                PlayerFunction playerFunction2 = (isPvE) ?
                        null : new PlayerFunction(gameState.player2(), new BoardController(gameState.player2().getBoard()));

                BotFunction botFunction = (playerFunction2 == null) ?
                        new BotFunction(gameState.player2(), new BoardController(gameState.player2().getBoard())) : null;
                GameFunction gameFunction = new GameFunction(
                            new Game(gameState),
                            playerFunction1,
                            playerFunction2,
                            botFunction,
                        botFunction != null,
                            gameState.itemEnabled()
                    );
                gameFunction.startGameLoop();
            } else {
                System.out.println("Không thể tải game. File lưu có thể bị hỏng.");
            }
        } else {
            System.out.println("Không có game nào để tiếp tục.");
        }
    }

}