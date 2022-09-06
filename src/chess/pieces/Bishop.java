package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {

	public Bishop(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "♝";
	}
	
	@Override
	public boolean[][] possibleMoves() {

		boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position aux = new Position(0, 0);
		
		// NW
		aux.setValues(position.getRow() - 1, position.getColumn() - 1);
		while (getBoard().positionExists(aux) && !getBoard().thereIsAPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
			aux.setValues(aux.getRow() - 1, aux.getColumn() - 1);
		}
		if (getBoard().positionExists(aux) && isThereOpponentPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}
		
		// NE
		aux.setValues(position.getRow() - 1, position.getColumn() + 1);
		while (getBoard().positionExists(aux) && !getBoard().thereIsAPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
			aux.setValues(aux.getRow() - 1, aux.getColumn() + 1);
		}
		if (getBoard().positionExists(aux) && isThereOpponentPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}
		
		// SE
		aux.setValues(position.getRow() + 1, position.getColumn() + 1);
		while (getBoard().positionExists(aux) && !getBoard().thereIsAPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
			aux.setValues(aux.getRow() + 1, aux.getColumn() + 1);
		}
		if (getBoard().positionExists(aux) && isThereOpponentPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}
		
		// SW
		aux.setValues(position.getRow() + 1, position.getColumn() - 1);
		while (getBoard().positionExists(aux) && !getBoard().thereIsAPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
			aux.setValues(aux.getRow() + 1, aux.getColumn() - 1);
		}
		if (getBoard().positionExists(aux) && isThereOpponentPiece(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}
		
		return moves;
	}
}
