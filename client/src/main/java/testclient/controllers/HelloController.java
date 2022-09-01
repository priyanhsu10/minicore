package testclient.controllers;


import minicore.contracts.ControllerBase;
import minicore.contracts.annotations.http.Delete;
import minicore.contracts.annotations.http.Get;
import minicore.contracts.annotations.http.Post;
import minicore.contracts.annotations.http.Put;
import minicore.contracts.annotations.modelBinding.FromBody;
import testclient.services.ITestService;
import testclient.services.Model;
import testclient.services.Model2;

import java.util.List;

public class HelloController  extends ControllerBase {
    private ITestService testService;

    public HelloController(ITestService testService) {
        this.testService = testService;
    }

    @Get(path = "/hello")
    public List<Model> get() {
        return this.testService.getlist();
    }
    @Post(path = "/hello")
    public Model post(@FromBody Model model){
        return model;
    }
    @Post(path = "/hello/m2")
    public Model post( Model2 model){
        return model.getModel();
    }
    @Put(path = "/helloupdate")
    public String put(String name){
        return "this is post "+name;
    }
    @Delete(path = "/helloDelete")
    public String delete(String name){
        return "this is post "+name;
    }

}
