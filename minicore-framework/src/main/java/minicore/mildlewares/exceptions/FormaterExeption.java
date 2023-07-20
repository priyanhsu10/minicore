package minicore.mildlewares.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class FormaterExeption extends MvcException {

    public FormaterExeption(Exception cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
