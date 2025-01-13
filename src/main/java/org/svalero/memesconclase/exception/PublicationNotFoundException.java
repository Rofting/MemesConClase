package org.svalero.memesconclase.exception;

public class PublicationNotFoundException extends Exception{
    public PublicationNotFoundException() {
        super("The publication does not exist");
    }

    public PublicationNotFoundException(String message) {
        super(message);
    }
}

