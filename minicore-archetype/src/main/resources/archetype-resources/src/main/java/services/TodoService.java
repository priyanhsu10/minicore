#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services;

import ${package}.exceptions.TestCustomException;

import java.util.*;
import java.util.stream.Collectors;

public class TodoService implements ITodoService {
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
