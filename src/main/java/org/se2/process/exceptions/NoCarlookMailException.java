package org.se2.process.exceptions;

public class NoCarlookMailException extends Throwable {
    private String reason;

    public NoCarlookMailException(String reason ) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

