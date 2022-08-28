package minicore.contracts.results;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IResultExecutionFilter;

import java.util.List;

public interface IResultExectutor {
    void executeResult(HttpContext context, List<IResultExecutionFilter> methodResultFilters);
}
