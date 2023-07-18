package io.testing.services;

import io.testing.exceptions.TestCustomException;

import java.util.*;
import java.util.stream.Collectors;

public class TestService implements ITestService {
    public final static Map<Integer, Model> modelList = new LinkedHashMap<>();
//testing for the circular dependency implementation
    //    private final ITest2 iTest2;

//    public TestService(ITest2 iTest2) {
//        this.iTest2 = iTest2;
//    }

    @Override
    public Model getById(int id) {
        if (modelList.containsKey(id)) {
            return modelList.get(id);
        }
        return null;
    }

    @Override
    public List<Model> getlist() {
        List<Model> models = modelList.values().stream().collect(Collectors.toList());
        return models;
    }

    @Override
    public Model create(Model model) {
        if (modelList.containsKey(model.getId())) {
            throw new TestCustomException("id :" + model.getId() + "aready existing");
        }
        modelList.put(model.getId(), model);
        return model;
    }
}
