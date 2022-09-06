package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class King extends ChessPiece {

	private final ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public String toString() {
		return "♛";
	}

	private boolean canMove(Position position) {
		ChessPiece piece = (ChessPiece)getBoard().piece(position);
		return isNull(piece) || piece.getColor() != getColor();
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece piece = (ChessPiece)getBoard().piece(position);
		return nonNull(piece) && piece instanceof Rook && piece.getColor() == getColor() && piece.getMoveCount() == 0;
	}
	
	@Override
	public boolean[][] possibleMoves() {

		boolean[][] moves = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position aux = new Position(0, 0);
		
		// ABOVE
		aux.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// BELOW
		aux.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// LEFT
		aux.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// RIGHT
		aux.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// NW
		aux.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// NE
		aux.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// SW
		aux.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}
		
		// SE
		aux.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(aux) && canMove(aux)) {
			moves[aux.getRow()][aux.getColumn()] = true;
		}

		// CASTLING
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {

			// CASTLING KINGSIDE ROOT
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCastling(posT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					moves[position.getRow()][position.getColumn() + 2] = true;
				}
			}

			// CASTLING QUEENSIDE ROOK
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
			if (testRookCastling(posT2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					moves[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}
		
		return moves;
	}
}
