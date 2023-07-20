package minicore.mildlewares.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class MvcException extends RuntimeException {
    public MvcException(String message) {
        super(message);
    }

    public MvcException(RuntimeException cause) {
        super(cause);
    }

    public MvcException(Exception exception) {
        super(exception);
    }
}