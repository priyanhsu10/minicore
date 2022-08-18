package minicore.testclient.controllers;


import minicore.contracts.ControllerBase;
import minicore.endpoints.annotations.Delete;
import minicore.endpoints.annotations.Get;
import minicore.endpoints.annotations.Post;
import minicore.endpoints.annotations.Put;

public class HelloController  extends ControllerBase {
    @Get(path = "/hello")
    public String get() {
        return "welcome";
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
