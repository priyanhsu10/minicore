package testclient.services;

import minicore.contracts.annotations.modelBinding.FromBody;
import minicore.contracts.annotations.modelBinding.FromHeader;

public class Model2 {
    public Model2() {
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @FromHeader(Key = "Authorization")
    private String token;
    @FromBody
    private Model model;

}
