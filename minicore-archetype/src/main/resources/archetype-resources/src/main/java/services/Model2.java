#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services;

import minicore.contracts.annotations.modelBinding.FromBody;
import minicore.contracts.annotations.modelBinding.FromHeader;
//Custom ModelBiding
public class Model2 {
    public Model2() {
    }
    @FromHeader(Key = "Authorization")
    private String token;

    //value bind from Body
    @FromBody
    private Model model;

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
    //value bind from header


}
