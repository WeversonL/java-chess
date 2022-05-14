package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import static chess.Color.WHITE;
import static java.util.Objects.isNull;

public class UserInterface {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static ChessPosition readChessPosition(Scanner sc) {

        try {
            String input = sc.nextLine().toUpperCase(Locale.ROOT);
            char column = input.charAt(0);
            int row = Integer.parseInt(input.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from A1 to H8");
        }

    }

    public static void printMatch(ChessMatch chessMatch) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());
        System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
    }

    public static void printBoard(ChessPiece[][] pieces) {

        for (int row = 0; row < pieces.length; row++) {

            System.out.print((8 - row) + " ");

            for (int column = 0; column < pieces.length; column++) {

                printPiece(pieces[row][column], false);

            }

            System.out.println();

        }

        System.out.println("  A B C D E F G H");

    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {

        for (int row = 0; row < pieces.length; row++) {

            System.out.print((8 - row) + " ");

            for (int column = 0; column < pieces.length; column++) {

                printPiece(pieces[row][column], possibleMoves[row][column]);

            }

            System.out.println();

        }

        System.out.println("  A B C D E F G H");

    }

    private static void printPiece(ChessPiece piece, boolean background) {

        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (isNull(piece)) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (piece.getColor() == WHITE) {
                System.out.print(piece + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");

    }

    public static String introduction() {
        return "King: ♛ | " +
                "Queen: ♚ | " +
                "Bishop: ♝ | " +
                "Horse: ♞ | " +
                "Rook: ♜ | " +
                "Pawn: ♟\n";
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
