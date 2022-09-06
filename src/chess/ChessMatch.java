package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import board.Board;
import board.Piece;
import board.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Horse;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;
import exception.ChessException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private final Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	private final List<Piece> piecesOnTheBoard;
	private final List<Piece> capturedPieces;
	
	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		piecesOnTheBoard = new ArrayList<>();
		capturedPieces = new ArrayList<>();
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public ChessPiece[][] getPieces() {
		ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getColumns()];
		for (int row = 0; row < board.getRows(); row++) {
			for (int column = 0; column < board.getColumns(); column++) {
				pieces[row][column] = (ChessPiece) board.piece(row, column);
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
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// specialmove promotion
		promoted = null;

		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer)));

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		// specialmove en passant
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece;
	}

	public ChessPiece replacePromotedPiece(String type) {
		if (isNull(promoted)) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("H") && !type.equals("R") && !type.equals("Q")) {
			return promoted;
		}
		
		Position position = promoted.getChessPosition().toPosition();
		Piece piece = board.removePiece(position);
		piecesOnTheBoard.remove(piece);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, position);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("H")) return new Horse(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	private Piece makeMove(Position source, Position target) {

		ChessPiece chessPiece = (ChessPiece)board.removePiece(source);
		chessPiece.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(chessPiece, target);
		
		if (nonNull(capturedPiece)) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// specialmove castling kingside rook
		if (chessPiece instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// specialmove castling queenside rook
		if (chessPiece instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}		
		
		// specialmove en passant
		if (chessPiece instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && isNull(capturedPiece)) {
				Position pawnPosition;
				if (chessPiece.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece chessPiece = (ChessPiece)board.removePiece(target);
		chessPiece.decreaseMoveCount();
		board.placePiece(chessPiece, source);
		
		if (nonNull(capturedPiece)) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// specialmove castling kingside rook
		if (chessPiece instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// specialmove castling queenside rook
		if (chessPiece instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// specialmove en passant
		if (chessPiece instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (chessPiece.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}
				else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
		if (board.piece(target) instanceof King) {
			throw new ChessException("Trying to bug the game isn't it? You can't capture the king without a CHECKMATE!");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = getPieceByColor(color);
		for (Piece piece : list) {
			if (piece instanceof King) {
				return (ChessPiece)piece;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = getPieceByColor(opponent(color));
		for (Piece piece : opponentPieces) {
			boolean[][] possibleMoves = piece.possibleMoves();
			if (possibleMoves[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = getPieceByColor(color);
		for (Piece piece : list) {
			boolean[][] possibleMoves = piece.possibleMoves();
			for (int row = 0; row < board.getRows(); row++) {
				for (int column = 0; column < board.getColumns(); column++) {
					if (possibleMoves[row][column]) {
						Position source = ((ChessPiece)piece).getChessPosition().toPosition();
						Position target = new Position(row, column);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void placeChessPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private List<Piece> getPieceByColor(Color color) {
		return piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
	}

	private void initialSetup() {
        placeChessPiece('A', 1, new Rook(board, Color.WHITE));
        placeChessPiece('B', 1, new Horse(board, Color.WHITE));
        placeChessPiece('C', 1, new Bishop(board, Color.WHITE));
        placeChessPiece('D', 1, new Queen(board, Color.WHITE));
        placeChessPiece('E', 1, new King(board, Color.WHITE, this));
		placeChessPiece('F', 1, new Bishop(board, Color.WHITE));
        placeChessPiece('G', 1, new Horse(board, Color.WHITE));
        placeChessPiece('H', 1, new Rook(board, Color.WHITE));
        placeChessPiece('A', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('B', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('C', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('D', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('E', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('F', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('G', 2, new Pawn(board, Color.WHITE, this));
        placeChessPiece('H', 2, new Pawn(board, Color.WHITE, this));

        placeChessPiece('A', 8, new Rook(board, Color.BLACK));
        placeChessPiece('B', 8, new Horse(board, Color.BLACK));
        placeChessPiece('C', 8, new Bishop(board, Color.BLACK));
        placeChessPiece('D', 8, new Queen(board, Color.BLACK));
        placeChessPiece('E', 8, new King(board, Color.BLACK, this));
        placeChessPiece('F', 8, new Bishop(board, Color.BLACK));
        placeChessPiece('G', 8, new Horse(board, Color.BLACK));
        placeChessPiece('H', 8, new Rook(board, Color.BLACK));
        placeChessPiece('A', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('B', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('C', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('D', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('E', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('F', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('G', 7, new Pawn(board, Color.BLACK, this));
        placeChessPiece('H', 7, new Pawn(board, Color.BLACK, this));
	}

}
