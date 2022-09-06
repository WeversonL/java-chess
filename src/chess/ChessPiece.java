package chess;

import board.Board;
import board.Piece;
import board.Position;

import static java.util.Objects.nonNull;

public abstract class ChessPiece extends Piece {

	private final Color color;
	private int moveCount;

	protected ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	protected void increaseMoveCount() {
		moveCount++;
	}

	protected void decreaseMoveCount() {
		moveCount--;
	}

	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece chessPiece = (ChessPiece)getBoard().piece(position);
		return nonNull(chessPiece) && chessPiece.getColor() != color;
	}
}
