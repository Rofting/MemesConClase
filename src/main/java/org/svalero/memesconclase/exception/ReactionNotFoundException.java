package org.svalero.memesconclase.exception;

public class ReactionNotFoundException extends Exception{

    public ReactionNotFoundException() {
        super("The reaction does not exist");
    }

    public ReactionNotFoundException(String message) {
        super(message);
    }
}
