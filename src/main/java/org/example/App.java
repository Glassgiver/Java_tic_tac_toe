package org.example;
import java.util.Scanner;
import java.util.Random;

public class App {
    private final static Scanner sc = new Scanner(System.in);
    private final static Random random = new Random();
    static char[][] map;
    static final int SIZE = 3;
    static final char DOT_PLAYER = 'X';
    static final char DOT_AI = '0';
    static final char DOT_EMPTY = '*';

    public static void main(String[] args) {
        playGame();

        while(true){
            System.out.println("Do you want to play again? y/n");
            String ans = sc.next();
            if (ans.equals("y")){
                playGame();
            }
            else break;
        }

    }

    private static void playGame(){
        prepareMap();
        printMap();

        while (true){
            playerTurn();
            printMap();
            if (isWin(DOT_PLAYER)){
                System.out.println("\nPlayer wins!");
                break;
            }
            if (isDraw()) {
                System.out.println("\nDraw!");
                break;
            }
            System.out.println();

            botTurn();
            printMap();
            if (isWin(DOT_AI)){
                System.out.println("\nAI wins!");
                break;
            }
            if (isDraw()) {
                System.out.println("\nDraw!");
                break;
            }
            System.out.println();
        }
    }

    private static boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    return false; // Найдена пустая клетка, игра не закончена
                }
            }
        }
        return true; // Все клетки заполнены, ничья
    }
    private static void botTurn() {
        int x, y;

        if (checkLine(DOT_AI, DOT_EMPTY)){
            return;
        }

        if (checkLine(DOT_PLAYER, DOT_EMPTY)) {
            return;
        }

        // Если не нашли строки, где нужно блокировать противника, делаем случайный ход
        do {
            x = random.nextInt(SIZE);
            y = random.nextInt(SIZE);
        } while (!isCellValid(x, y));
        map[y][x] = DOT_AI;
    }

    private static boolean checkLine(char playerSymbol, char emptySymbol) {
        // Проверка строк и столбцов
        for (int i = 0; i < SIZE; i++) {
            int countPlayerRow = 0;
            int countEmptyRow = 0;
            int countPlayerCol = 0;
            int countEmptyCol = 0;
            int emptyIndexRow = -1;
            int emptyIndexCol = -1;

            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == playerSymbol) {
                    countPlayerRow++;
                } else if (map[i][j] == emptySymbol) {
                    countEmptyRow++;
                    emptyIndexRow = j;
                }

                if (map[j][i] == playerSymbol) {
                    countPlayerCol++;
                } else if (map[j][i] == emptySymbol) {
                    countEmptyCol++;
                    emptyIndexCol = j;
                }
            }

            if (countPlayerRow == 2 && countEmptyRow == 1) {
                map[i][emptyIndexRow] = DOT_AI;
                return true;
            }

            if (countPlayerCol == 2 && countEmptyCol == 1) {
                map[emptyIndexCol][i] = DOT_AI;
                return true;
            }
        }

        // Проверка главной диагонали
        int countPlayerDiag1 = 0;
        int countEmptyDiag1 = 0;
        int emptyIndexDiag1 = -1;
        for (int i = 0; i < SIZE; i++) {
            if (map[i][i] == playerSymbol) {
                countPlayerDiag1++;
            } else if (map[i][i] == emptySymbol) {
                countEmptyDiag1++;
                emptyIndexDiag1 = i;
            }
        }

        if (countPlayerDiag1 == 2 && countEmptyDiag1 == 1) {
            map[emptyIndexDiag1][emptyIndexDiag1] = DOT_AI;
            return true;
        }

        // Проверка побочной диагонали
        int countPlayerDiag2 = 0;
        int countEmptyDiag2 = 0;
        int emptyIndexDiag2 = -1;
        for (int i = 0; i < SIZE; i++) {
            if (map[i][SIZE - 1 - i] == playerSymbol) {
                countPlayerDiag2++;
            } else if (map[i][SIZE - 1 - i] == emptySymbol) {
                countEmptyDiag2++;
                emptyIndexDiag2 = i;
            }
        }

        if (countPlayerDiag2 == 2 && countEmptyDiag2 == 1) {
            map[emptyIndexDiag2][SIZE - 1 - emptyIndexDiag2] = DOT_AI;
            return true;
        }

        return false;
    }



    public static void playerTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты в формате X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(x, y));
        map[y][x] = DOT_PLAYER;
    }
    public static boolean isCellValid(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return false;
        }
        return map[y][x] == DOT_EMPTY;
    }

    public static void printMap(){
        for (int j = 0; j <= SIZE; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");

            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void prepareMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    private static boolean isWin(char player) {
        boolean rows, cols, diag1, diag2;
        diag1 = true;
        diag2 = true;

        for (int i = 0; i < SIZE; i++) {
            rows = true;
            cols = true;

            // Проверяем диагонали
            diag1 &= (map[i][i] == player); // Проверка главной диагонали
            diag2 &= (map[i][SIZE - 1 - i] == player); // Проверка побочной диагонали

            for (int j = 0; j < SIZE; j++) {
                rows &= (map[i][j] == player); // Проверка строки
                cols &= (map[j][i] == player); // Проверка столбца
            }

            // Если в любой строке или столбце все элементы равны player, возвращаем true
            if (rows || cols) {
                return true;
            }
        }

        // Проверяем диагонали после цикла
        if (diag1 || diag2) {
            return true;
        }

        return false;
    }

}