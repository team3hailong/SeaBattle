package model.game;

import java.io.Serial;
import java.io.Serializable;

import static view.EventInterface.reset;

public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public int size;
    public Cell[][] cells;

    public void printShip(String shipIcon, Cell[][] cells, int row, int col) {
        Ship ship = cells[row][col].getShip();
        System.out.print(ship.getColorCode()+shipIcon);
        if (col+1>=size || cells[row][col+1].getShip() != cells[row][col].getShip()) {
            System.out.print(reset);
        }
        System.out.print(" "+reset);
    }

    public Board(int size) {
        this.size = size;
        cells = new Cell[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < size; row++) {
            char xAris = (char) ('A' + row);
            for (int yAris = 0; yAris < size; yAris++) {
                cells[row][yAris] = new Cell(xAris, yAris + 1);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public boolean isCoordinateValid(char row, int col) {
        if (row < 'A' || row >= ('A' + size)) return false;
        return col >= 1 && col <= size;
    }

    public boolean isNotValidCoordinateFormat(String coordinate) {
        if (coordinate.length() < 2) {
            return true;
        }
        char x = coordinate.charAt(0);
        String yPart = coordinate.substring(1);
        return !Character.isLetter(x) || !yPart.matches("\\d+");
    }

    public Cell getCell(char xAris, int yAris) {
        if (!isCoordinateValid(xAris, yAris)) return null;
        int row = xAris - 'A';
        int col = yAris - 1;
        return cells[row][col];
    }

    public void displayBoardsSideBySide(Board fogBoard) {
        String headerFormat = String.format("%%-%ds     %%-%ds%n", size * 3 + 3, size * 3 + 3);
        if(fogBoard!=this) System.out.printf(headerFormat, "Bảng của bạn", "Bảng sương mù");
        else System.out.printf(headerFormat, "Bảng của bạn", "");

        printColumnHeaders(fogBoard != this);

        for (int xNumber = 0; xNumber < size; xNumber++) {
            char xAris = (char) ('A' + xNumber);
            System.out.print(xAris + "| ");
            for (int yAris = 0; yAris < size; yAris++) {
                Cell cell = this.cells[xNumber][yAris];
                switch (cell.getStatus()) {
                    case HIT:
                        if(cell.getShip().isSunk())
                            printShip("\uD83D\uDC7B", cells, xNumber, yAris);
                        else
                            printShip("\uD83D\uDCA5", cells, xNumber, yAris);
                        break;
                    case MISS:
                        System.out.print("❌ ");
                        break;
                    case EMPTY:
                        System.out.print("🌊 ");
                        break;
                    case SHIP:
                        printShip("🚢", cells, xNumber, yAris);
                }
            }

            if(fogBoard!=this) {
                System.out.print(" ~~~ ");
                System.out.print(xAris + "| ");
                for (int yAris = 0; yAris < fogBoard.getSize(); yAris++) {
                    Cell cell = fogBoard.getCell((char) ('A' + xNumber), yAris + 1);
                    Cell[][] cells = fogBoard.getCells();
                    switch (cell.getStatus()) {
                        case HIT:
                            if (cell.getShip().isSunk())
                                printShip("\uD83D\uDC7B", cells, xNumber, yAris);
                            else System.out.print("💥 ");
                            break;
                        case MISS:
                            System.out.print("❌ ");
                            break;
                        default:
                            System.out.print("🌊 ");
                    }
                }
            }
            System.out.println();
        }

        printLegend();
    }

    private void printColumnHeaders(boolean hasFogBoard) {
        System.out.print("  ");
        for (int yourColumnIndex = 1; yourColumnIndex <= size; yourColumnIndex++){
            System.out.print(" " + yourColumnIndex);
            if(yourColumnIndex<10) System.out.print(" ");
        }
        if(hasFogBoard) {
            System.out.print("  ~~~   ");
            for (int myColumnIndex = 1; myColumnIndex <= size; myColumnIndex++){
                System.out.print(" " + myColumnIndex);
                if(myColumnIndex<10) System.out.print(" ");
            }
        }
        System.out.println();

        System.out.print(" +");
        for (int border = 1; border <= size; border++) {
            System.out.print("---");
        }
        if(hasFogBoard) {
            System.out.print("+");
            System.out.print(" ~~~ ");
            System.out.print(" +");
            for (int border = 1; border <= size; border++) {
                System.out.print("---");
            }
        }
        System.out.println("+");
    }

    private void printLegend() {
        System.out.println("=== Phần chú thích ===");
        System.out.println("Bảng của bạn:");
        System.out.println("🚢: Ô chứa tàu   🌊: Không có tàu     ❌: Địch bắn trượt   \uD83D\uDC7B: Địch bắn chìm     \uD83D\uDCA5: Đich bắn trúng");
        System.out.println("Bảng sương mù");
        System.out.println("🌊: Ô chưa bắn     ❌: Ta bắn trượt   \uD83D\uDC7B: Ta bắn chìm     \uD83D\uDCA5: Ta bắn trúng");
        System.out.println("\nCác loại tàu:");
        Ship.shipsList();
        System.out.println();
    }
}