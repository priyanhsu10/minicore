package minicore.contracts.results;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IFilterProvider;
import minicore.contracts.filters.IResultExecutionFilter;

import java.util.List;

public class ResultExecutor implements IResultExectutor {
    private final IFilterProvider iFilterProvider;


    public ResultExecutor(IFilterProvider iFilterProvider) {
        this.iFilterProvider = iFilterProvider;
    }

    @Override
    public void executeResult(HttpContext context,List<IResultExecutionFilter> methodResultFilters) {
         //step 1. execute the result filters
        executResultFilters(context,  this.iFilterProvider.getResultExecutionFilters());
        executResultFilters(context, methodResultFilters);
        //3.exectute result
        context.ActionContext.ActionResult.executeResult(context);
    }

    private void executResultFilters(HttpContext context, List<IResultExecutionFilter> resultFilters) {
        for (int i = 0; i< resultFilters.size(); i++){
            resultFilters.get(i).beforeResultExecute(context);
        }
    }
}
