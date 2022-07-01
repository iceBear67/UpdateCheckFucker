package io.ib67.ucf.exception;

public class UpdateException extends RuntimeException{
    public UpdateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return new StackTraceElement[]{
                new StackTraceElement(super.getMessage(),"","blocked",233)
        };
    }
}
