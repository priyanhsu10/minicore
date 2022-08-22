package minicore.pipeline;

import com.sun.tools.javac.code.Attribute;
import minicore.contracts.IAction;
import minicore.contracts.IMiddleware;
import minicore.contracts.pipeline.IApplicationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PipelineBuilder implements IApplicationBuilder {
    private IAction initialAction;
    public List<Class<? extends IMiddleware>> middlewareTypes = new ArrayList<>();
    private Map<String,IAction> actionMapList= new HashMap<>();
    public PipelineBuilder(IAction action) {
        this.initialAction = action;

    }

    @Override
    public IApplicationBuilder use(Class<? extends IMiddleware> middlewareType) {
        middlewareTypes.add(middlewareType);
        return this;
    }
    @Override
    public IApplicationBuilder map(String url,IAction action) {
        actionMapList.put(url,action);
        return this;
    }


    private IAction createPipeline(int index) {
        if (index < (middlewareTypes.size() - 1)) {
            //create handler and pass context
            IAction actionHandler = createPipeline(index + 1);

            return build(index, actionHandler);
        } else {
            return build(index, null);
        }

    }

    @Override
    public IAction build() {
        return createPipeline(0);
    }

    private IAction build(int index, IAction action) {
        if (action == null) {
            action = initialAction;
        }
        IMiddleware middleware = null;
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
        return middleware::invoke;

    }
}
