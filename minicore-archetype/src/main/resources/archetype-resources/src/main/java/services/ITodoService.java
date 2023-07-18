#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services;

import java.util.List;

public interface ITodoService {
    List<Model> getlist();

    Model getById(int id);
    Model create(Model model);
}

