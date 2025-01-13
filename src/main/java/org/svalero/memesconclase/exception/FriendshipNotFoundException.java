package org.svalero.memesconclase.exception;

public class FriendshipNotFoundException extends Exception{

    public FriendshipNotFoundException() {
        super("The friendship does not exist");
    }

    public FriendshipNotFoundException(String message) {
        super(message);
    }
}
