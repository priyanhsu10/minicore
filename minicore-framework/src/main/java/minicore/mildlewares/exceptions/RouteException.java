package minicore.mildlewares.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class RouteException extends MvcException {
    private String Httpstatus;

    public String getHttpstatus() {
        return Httpstatus;
    }

    public void setHttpstatus(String httpstatus) {
        Httpstatus = httpstatus;
    }

    public RouteException(String message, String HttpStatus) {
        super(message);
    }
}