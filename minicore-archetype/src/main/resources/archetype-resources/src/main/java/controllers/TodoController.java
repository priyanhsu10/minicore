#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controllers;
import minicore.configuration.IConfiguration;
import minicore.contracts.ControllerBase;
import minicore.contracts.annotations.filters.ActionFilter;
import minicore.contracts.annotations.filters.ResultFilter;
import minicore.contracts.annotations.http.*;
import minicore.contracts.annotations.modelBinding.FromBody;
import minicore.contracts.annotations.modelBinding.FromQuery;
import ${package}.filters.CustomResultFilter;
import  ${package}.filters.TestActionFilter;
import ${package}.filters.TestActionFilter2;
import ${package}.services.ITodoService;
import ${package}.services.Model;
import ${package}.services.Model2;

import java.util.List;
import java.util.stream.Collectors;

@Route(path = "/todo")
@ActionFilter(filterClass = TestActionFilter2.class)
public class TodoController  extends ControllerBase {
    private ITodoService todoService;
    private final IConfiguration iConfiguration;

    public TodoController(ITodoService todoService, IConfiguration iConfiguration) {
        this.todoService = todoService;
        this.iConfiguration = iConfiguration;
    }

    @Get
    @ActionFilter(filterClass = TestActionFilter.class)
    //apply custom filter for specific action
    @ResultFilter(filterClass = CustomResultFilter.class)
    public List<Model> get() {
      Integer value=  iConfiguration.getValue(Integer.class,"age");
        System.out.println("value from Conguraton age:"+value);
        //accesing transaiton id which is added by transactionIdMiddlware
        System.out.println(httpContext.getData().get("trazId"));
        return this.todoService.getlist();

    }

    @Get(path = "/:id") //value bind from model
    public Model get(int id) {
        return this.todoService.getById(id);
    }

    @Get(path = "/filter")//value bind from query
    public List<Model> get(@FromQuery String name) {
        return this.todoService.getlist().stream().filter(x->x.getName().equals(name)).collect(Collectors.toList());
    }
    @Post
    //Direct Model binding use @FromBody annotation
    public Model post(@FromBody Model model){
        return todoService.create(model);
    }
    @Post(path = "/custom")
    //Custom ModelBiding
    public Model post(Model2 model){
        return model.getModel();
    }
    @Put(path = "/update")
    public String put(String name){
        return "this is post "+name;
    }
    @Delete(path = "/delete")
    public String delete(String name){
        return "this is post "+name;
    }

}
