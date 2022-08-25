package minicore.contracts.results;

public class ActionResult implements IActionResult{
    protected final Object value;

    public ActionResult(Object  value) {
        this.value = value;
    }
}
