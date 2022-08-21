package testclient.controllers;


import minicore.contracts.ControllerBase;
import minicore.endpoints.annotations.Delete;
import minicore.endpoints.annotations.Get;
import minicore.endpoints.annotations.Post;
import minicore.endpoints.annotations.Put;
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
    public String post(String name){
        return "this is post "+name;
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
