package view;

import java.io.File;
import static view.EventInterface.*;


public class MenuInterface {
    public static File file = new File("tempGame.txt");

    private static class Holder {
        private static final MenuInterface INSTANCE = new MenuInterface();
    }

    public static MenuInterface getInstance() {
        return Holder.INSTANCE;
    }

    public void seaBattle(){
        System.out.print(
                " ███████╗███████╗ █████╗ ██████╗  █████╗ ████████╗████████╗██╗     ███████╗ \n" +
                " ██╔════╝██╔════╝██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝╚══██╔══╝██║     ██╔════╝ \n" +
        blue +
                " ███████╗█████╗  ███████║██████╔╝███████║   ██║      ██║   ██║     █████╗   \n" +
                " ╚════██║██╔══╝  ██╔══██║██╔══██╗██╔══██║   ██║      ██║   ██║     ██╔══╝   \n" +
                " ███████║███████╗██║  ██║██████╔╝██║  ██║   ██║      ██║   ███████╗███████╗ \n" +
                " ╚══════╝╚══════╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚══════╝╚══════╝ \n" +
        blue +
                "                              1. Chơi mới                                   \n" +
        reset +
                "                       2. Hiển thị bảng xếp hạng                            \n" +
        blue +
                "                             3. Bật/Tắt âm                                  \n" +
        reset +
                "                             4. Luật chơi                                  \n" +
                reset);
        if (file.length() > 0) {
            System.out.print(green +
                "                              5. Tiếp tục                                   \n" +
                reset);
        }
        System.out.println(red +
                "                               0. Thoát                                     \n" +
                reset);
    }

    public String gameplayPicker(){
        return green + "1. Chế độ 2 người chơi" + blue + "\n2. Đấu với máy" + reset +
                "\nHãy chọn chế độ chơi (Nếu bạn chọn số khác 1, bạn sẽ chơi với máy): ";
    }

    public void instructions() {
        System.out.println("+------------------------Hướng dẫn chơi-------------------------------------------+");
        System.out.println("| 1. Trò chơi có hai chế độ: Chơi đơn và Chơi hai người.                          |");
        System.out.println("|    - Chế độ Chơi đơn: Người chơi đấu với máy (Mức Dễ hoặc Khó).                 |");
        System.out.println("|    - Chế độ Chơi hai người: Hai người chơi đấu với nhau.                        |");
        System.out.println("| 2. Người chơi có thể chọn kích thước bảng phù hợp.                              |");
        System.out.println("| 3. Mỗi người chơi bí mật đặt tàu (tự động/thủ công) trên bảng của mình.         |");
        System.out.println("| 4. Người chơi luân phiên bắn nhau bằng cách chọn một ô trên bảng địch.          |");
        System.out.println("| 5. Khi một tàu bị bắn hết các ô, tàu đó bị chìm.                                |");
        System.out.println("| 6. Người chiến thắng là người bắn chìm hết tàu của đối phương.                  |");
        System.out.println("| 7. Hiển thị bảng xếp hạng để theo dõi điểm số của người chơi.                   |");
        System.out.println("| 8. Sử dụng các vật phẩm như Bom, Khiên và Đèn để hỗ trợ trong trò chơi.         |");
        System.out.println("|    - Bom: Phát nổ toàn bộ khu vực lân cận ô được chọn, diện tích 2x2            |");
        System.out.println("|    - Khiên: Bảo vệ tàu chứa ô được chọn, chỉ có tác dụng 1 lần                  |");
        System.out.println("|    - Đèn: Cho biết ô được chọn có tàu xuất hiện hay không                       |");
        System.out.println("| 9. Có thể bật hoặc tắt âm thanh theo ý muốn.                                    |");
        System.out.println("| 10. Bạn có thể lưu và tiếp tục tiến trình trò chơi để chơi sau.                 |");
        System.out.println("+---------------------------------------------------------------------------------+");
    }
}
