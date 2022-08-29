package testclient.controllers;


import minicore.contracts.ControllerBase;
import minicore.contracts.annotations.http.Delete;
import minicore.contracts.annotations.http.Get;
import minicore.contracts.annotations.http.Post;
import minicore.contracts.annotations.http.Put;
import testclient.services.ITestService;
import testclient.services.Model;

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
    public Model post(Model model){
        return model;
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
