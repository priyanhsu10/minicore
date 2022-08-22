package minicore.contracts.filters;

import minicore.contracts.IAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.logging.Filter;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ActionFilter {

    Class<? extends IActionFilter> filterClass();

}
@ActionFilter(filterClass = MyActionFilter.class)
class test{
     @ActionFilter(filterClass = MyActionFilter.class)
     public  void a(){}
}
class  MyActionFilter implements IActionFilter{

     @Override
     public void beforeExecute() {
          System.out.println("before execute ");
     }

     @Override
     public void afterExecute() {
          System.out.println("after execute ");

     }
}