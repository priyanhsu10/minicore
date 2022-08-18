package minicore.pipeline;

import minicore.contracts.HttpContext;
import minicore.contracts.IAction;
import minicore.contracts.IMiddleware;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PipelineBuilder {
    private IAction initialAction;
    private  IAction finalAction;
    public List<Class<? extends IMiddleware>> middlewareTypes = new ArrayList<>();

    public PipelineBuilder(IAction action) {
        this.initialAction = action;

    }

    public PipelineBuilder use(Class<? extends IMiddleware> middlewareType) {
        middlewareTypes.add(middlewareType);
        return  this;
    }

    private IAction createPipeline(int index) {
        if (index < (middlewareTypes.size() - 1)) {
            //create handler and pass context
            IAction actionHandler = createPipeline(index + 1);

            return build(index,actionHandler);
        } else {
            return build(index,null);
        }

    }
public  void execute(HttpContext c) throws Exception {
        if(finalAction==null){
            finalAction=build();
        }
    finalAction.next(c);
}
    public  IAction build() {
      return   createPipeline(0);
    }
    private  IAction build(int index ,IAction action){
        if(action==null){
              action=initialAction;
        }
        IMiddleware middleware= null;
        try {
            middleware = middlewareTypes.get(index).getConstructor(IAction.class).newInstance(action);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return  middleware::invoke;

    }
}
