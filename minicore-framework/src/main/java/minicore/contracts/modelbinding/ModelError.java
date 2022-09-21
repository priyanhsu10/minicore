package minicore.contracts.modelbinding;

import java.util.HashMap;
import java.util.Map;

public class ModelError {


    private  String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    private Map<String,String> errors= new HashMap<>();
}
