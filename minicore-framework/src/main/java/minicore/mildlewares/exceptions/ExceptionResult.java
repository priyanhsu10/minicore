package minicore.mildlewares.exceptions;

public class ExceptionResult {
    private Error error;

    public ExceptionResult(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
