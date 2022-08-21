package testclient.services;

import java.util.Arrays;
import java.util.List;

public class TestService implements  ITestService{
    @Override
    public Model getData() {
        return  new Model("priyanshu",32);
    }

    @Override
    public List<Model> getlist() {
        return Arrays.asList(new Model("asshu",5),new Model("priyanshu",32));
    }
}
