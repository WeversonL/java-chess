package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ChessException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        System.out.println(UserInterface.introduction());

        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        while (1 == 1) {

            try {

                UserInterface.clearScreen();
                UserInterface.printMatch(chessMatch);
                System.out.println();

                System.out.print("Source: ");
                ChessPosition source = UserInterface.readChessPosition(scanner);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UserInterface.clearScreen();
                UserInterface.printBoard(chessMatch.getPieces(), possibleMoves);
                System.out.println();

                System.out.print("Target: ");
                ChessPosition target = UserInterface.readChessPosition(scanner);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                System.out.println();
                System.out.print("[Press any key to continue...]");
                scanner.nextLine();
            }

        }

    }
}
