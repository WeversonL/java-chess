package main.java.chess.pieces;

import main.java.board.Board;
import main.java.board.Position;
import main.java.chess.ChessPiece;
import main.java.chess.Color;

import static java.util.Objects.isNull;

public class Horse extends ChessPiece {

    public Horse(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "♞";
    }

    private boolean canMove(Position position) {
        ChessPiece chessPiece = (ChessPiece) getBoard().piece(position);
        return isNull(chessPiece) || chessPiece.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {

        boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position aux = new Position(0, 0);

        aux.setValues(position.getRow() - 1, position.getColumn() - 2);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() - 2, position.getColumn() - 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() - 2, position.getColumn() + 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() - 1, position.getColumn() + 2);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() + 1, position.getColumn() + 2);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() + 2, position.getColumn() + 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() + 2, position.getColumn() - 1);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        aux.setValues(position.getRow() + 1, position.getColumn() - 2);
        if (getBoard().positionExists(aux) && canMove(aux)) {
            moves[aux.getRow()][aux.getColumn()] = true;
        }

        return moves;
    }
}
