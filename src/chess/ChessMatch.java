package chess;

import board.Board;
import board.Piece;
import board.Position;
import chess.pieces.King;
import chess.pieces.Rook;
import exception.ChessException;

import static chess.Color.BLACK;
import static chess.Color.WHITE;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = WHITE;
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessPiece[][] getPieces() {

        ChessPiece[][] pieces = new ChessPiece[board.getColumns()][board.getColumns()];

        for (int rows = 0; rows < board.getRows(); rows++) {

            for (int columns = 0; columns < board.getColumns(); columns++) {

                pieces[rows][columns] = (ChessPiece) board.piece(rows, columns);

            }

        }

        return pieces;

    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {

        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        return (ChessPiece) capturedPiece;

    }

    private Piece makeMove(Position source, Position target) {
        Piece removedPiece = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(removedPiece, target);
        return capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The thosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void placeChessPiece(char column, int row, Piece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == WHITE) ? BLACK : WHITE;
    }

    private void initialSetup() {
        placeChessPiece('C', 1, new Rook(board, Color.WHITE));
        placeChessPiece('C', 2, new Rook(board, Color.WHITE));
        placeChessPiece('D', 2, new Rook(board, Color.WHITE));
        placeChessPiece('E', 2, new Rook(board, Color.WHITE));
        placeChessPiece('E', 1, new Rook(board, Color.WHITE));
        placeChessPiece('D', 1, new King(board, Color.WHITE));

        placeChessPiece('C', 7, new Rook(board, Color.BLACK));
        placeChessPiece('C', 8, new Rook(board, Color.BLACK));
        placeChessPiece('D', 7, new Rook(board, Color.BLACK));
        placeChessPiece('E', 7, new Rook(board, Color.BLACK));
        placeChessPiece('E', 8, new Rook(board, Color.BLACK));
        placeChessPiece('D', 8, new King(board, Color.BLACK));
    }

}