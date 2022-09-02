package testclient.services;

import testclient.exceptions.TestCustomException;

import java.util.*;
import java.util.stream.Collectors;

public class TestService implements ITestService {
    public final static Map<Integer, Model> modelList = new LinkedHashMap<>();

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
