package main.java.exception;

public class BoardException extends RuntimeException {
    private static final long serialVersionUID = 6854487260014850809L;

    public BoardException(String msg) {
        super(msg);
    }
}
