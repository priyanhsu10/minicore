package minicore.contracts.results;

public class ModelValidationResult extends ObjectResult {

    public ModelValidationResult(Object value, int httpStatus) {
        super(value, httpStatus);
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int getHttpStatus() {
        return HttpStatus.BadRequest;
    }
}