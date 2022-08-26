package minicore.contracts.filters;

import minicore.contracts.HttpContext;

public interface IExceptionFilter {

    boolean support(Class<? extends RuntimeException> excetpions);

   <T extends  RuntimeException> void onException(HttpContext context, T e);
}
