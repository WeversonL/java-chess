package main.java.application;

import main.java.chess.ChessMatch;
import main.java.chess.ChessPiece;
import main.java.chess.ChessPosition;

import java.util.*;

import static java.util.Objects.isNull;
import static main.java.chess.Color.BLACK;
import static main.java.chess.Color.WHITE;

public class UserInterface {

    private UserInterface() {
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

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

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn : " + chessMatch.getTurn());
        if (!chessMatch.getCheckMate()) {
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("The " + chessMatch.getCurrentPlayer() + " player is in CHECK");
            }
        } else {
            System.out.println("----------");
            System.out.println("CHECKMATE");
            System.out.println("Winner: " + chessMatch.getCurrentPlayer());
            System.out.println("----------");
        }
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

    private static void printCapturedPieces(List<ChessPiece> captured) {
        System.out.println("-> Captured pieces <-");
        System.out.print("White: ");
        System.out.println(Arrays.toString(captured.stream().filter(x -> x.getColor() == WHITE).toArray()));
        System.out.print("Black: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(captured.stream().filter(x -> x.getColor() == BLACK).toArray()));
        System.out.print(ANSI_RESET);
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
