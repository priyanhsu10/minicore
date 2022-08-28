package minicore.contracts.common;

public class Action {
    public  static void execute(IActionExecutor action){
        try{
          action.execute();
        }
        catch (RuntimeException e){
            //execption handler
            throw  e;
        }
    }
}
