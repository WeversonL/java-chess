package board;

public abstract class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        this.position = null;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] positions = possibleMoves();

        for (int row = 0; row < positions.length; row++) {

            for (int column = 0; column < positions.length; column++) {

                if (positions[row][column]) {
                    return true;
                }

            }

        }

        return false;

    }

}
