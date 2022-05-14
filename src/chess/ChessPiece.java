package chess;

import board.Board;
import board.Piece;
import board.Position;

import static java.util.Objects.nonNull;

public abstract class ChessPiece extends Piece {

    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    protected boolean isThereOpponentPiece(Position position) {

        ChessPiece pieceInPosition = (ChessPiece) getBoard().piece(position);

        return nonNull(pieceInPosition) && pieceInPosition.getColor() != color;

    }

}
