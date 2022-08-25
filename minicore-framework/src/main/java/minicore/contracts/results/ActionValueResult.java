package minicore.contracts.results;

public class ActionValueResult<T> implements IConvertToActionResult {
    T value;

    public ActionValueResult(T value) {
        this.value = value;
    }

    public IActionResult convert() {
        return new ObjectResult(value);
    }
}
