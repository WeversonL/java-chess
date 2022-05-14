package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessPiece;
import chess.Color;

import static java.util.Objects.isNull;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "♛";
    }

    private boolean canMove(Position position) {
        ChessPiece piecePosition = (ChessPiece) getBoard().piece(position);
        return isNull(piecePosition) || piecePosition.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {

        boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position aux = new Position(0, 0);

        // above
        aux.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // bellow
        aux.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // left
        aux.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // rigth
        aux.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // nw
        aux.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // ne
        aux.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // sw
        aux.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        // se
        aux.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        return moves;

    }

}
