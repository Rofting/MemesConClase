package org.svalero.memesconclase.exception;

public class CommentNotFoundException extends Exception{

    public CommentNotFoundException() {
        super("The comment does not exist");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
