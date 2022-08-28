package minicore.mvc.filters;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class MvcException extends  RuntimeException{
    public MvcException(String message) {
        super(message);
    }

    public MvcException(RuntimeException cause) {
        super(cause);
    }
}
