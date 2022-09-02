package testclient.services;

import java.util.List;

public interface ITestService {
    List<Model> getlist();

    Model getById(int id);
    Model create(Model model);
}

