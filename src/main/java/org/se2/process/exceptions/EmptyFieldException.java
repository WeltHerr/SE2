package org.se2.process.exceptions;

public class EmptyFieldException extends Exception {
    private String reason;


    public EmptyFieldException( String reason ) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
