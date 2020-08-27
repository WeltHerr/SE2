package org.se2.process.exceptions;

public class EmailInUseException extends Throwable {
    private String reason;


    public EmailInUseException(String reason ) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
