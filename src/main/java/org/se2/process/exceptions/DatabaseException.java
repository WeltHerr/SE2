package org.se2.process.exceptions;

public class DatabaseException extends Exception{
    private String reason;


    public DatabaseException( String reason ) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
