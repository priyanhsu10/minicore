package minicore.contracts.filters;

import java.util.List;

public interface IFilterProvider {
    List<IActionFilter>  getGlobalFilters();
    List<IAuthenticationFilter>  getAuthFilters();
    List<IExceptionFilter>  getexceptionFilters();
}
