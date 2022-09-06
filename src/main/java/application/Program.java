package main.java.application;

import main.java.chess.ChessMatch;
import main.java.chess.ChessPiece;
import main.java.chess.ChessPosition;
import main.java.exception.ChessException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static java.util.Objects.nonNull;

public class Program {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()) {
            try {
                UserInterface.clearScreen();
                UserInterface.printMatch(chessMatch, captured);
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UserInterface.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UserInterface.clearScreen();
                UserInterface.printBoard(chessMatch.getPieces(), possibleMoves);
                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UserInterface.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                if (nonNull(capturedPiece)) {
                    captured.add(capturedPiece);
                }

                if (nonNull(chessMatch.getPromoted())) {
                    System.out.print("Enter piece for promotion (B/H/R/Q): ");
                    String type = sc.nextLine().toUpperCase();
                    while (!type.equals("B") && !type.equals("H") && !type.equals("R") & !type.equals("Q")) {
                        System.out.print("Invalid value! Enter piece for promotion (B/H/R/Q): ");
                        type = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(type);
                }
            } catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                System.out.println();
                System.out.print("[Press any key to continue...]");
                sc.nextLine();
            }
        }
        UserInterface.clearScreen();
        UserInterface.printMatch(chessMatch, captured);
    }
}
