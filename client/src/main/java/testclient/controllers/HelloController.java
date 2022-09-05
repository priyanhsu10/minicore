package testclient.controllers;


import minicore.configuration.IConfiguration;
import minicore.contracts.ControllerBase;
import minicore.contracts.annotations.filters.ActionFilter;
import minicore.contracts.annotations.filters.ResultFilter;
import minicore.contracts.annotations.http.*;
import minicore.contracts.annotations.modelBinding.FromBody;
import minicore.contracts.annotations.modelBinding.FromQuery;
import testclient.filters.CustomResultFilter;
import testclient.filters.TestActionFilter;
import testclient.filters.TestActionFilter2;
import testclient.services.ITestService;
import testclient.services.Model;
import testclient.services.Model2;

import java.util.List;
import java.util.stream.Collectors;

@Route(path = "/hello")
@ActionFilter(filterClass = TestActionFilter2.class)
public class HelloController  extends ControllerBase {
    private ITestService testService;
    private final IConfiguration iConfiguration;

    public HelloController(ITestService testService, IConfiguration iConfiguration) {
        this.testService = testService;
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
        return this.testService.getlist();

    }

    @Get(path = "/:id") //value bind from model
    public Model get(int id) {
        return this.testService.getById(id);
    }

    @Get(path = "/filter")//value bind from query
    public List<Model> get(@FromQuery String name) {
        return this.testService.getlist().stream().filter(x->x.getName().equals(name)).collect(Collectors.toList());
    }
    @Post
    //Direct Model binding use @FromBody annotation
    public Model post(@FromBody Model model){
        return testService.create(model);
    }
    @Post(path = "/m2")
    //Custom ModelBiding
    public Model post( Model2 model){
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
