package minicore.contracts.filters;

import minicore.contracts.HttpContext;

public interface IExceptionFilter {

    boolean support(Class<? extends RuntimeException> exceptions);

   <T extends  RuntimeException> void onException(HttpContext context, T e);
}
