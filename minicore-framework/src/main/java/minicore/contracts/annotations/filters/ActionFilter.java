package minicore.contracts.annotations.filters;

import minicore.contracts.HttpContext;
import minicore.contracts.filters.IActionFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ActionFilter  {

    Class<? extends IActionFilter> filterClass();

}
@ActionFilter(filterClass = MyActionFilter.class)
@Authorize
class test{
     @ActionFilter(filterClass = MyActionFilter.class)
     public  void a(){}
}
class  MyActionFilter implements IActionFilter{

     @Override
     public void beforeExecute(HttpContext  httpContext) {
          System.out.println("before execute ");
     }

     @Override
     public void afterExecute(HttpContext  httpContext) {
          System.out.println("after execute ");

     }
}