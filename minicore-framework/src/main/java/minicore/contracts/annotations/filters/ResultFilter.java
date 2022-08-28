package minicore.contracts.annotations.filters;

import minicore.contracts.filters.IActionFilter;
import minicore.contracts.filters.IResultExecutionFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResultFilter {
    Class<? extends IResultExecutionFilter> filterClass();
}
