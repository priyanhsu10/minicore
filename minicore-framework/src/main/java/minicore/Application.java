package minicore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import minicore.contracts.host.IStartup;
import minicore.contracts.ioc.IServiceCollection;
import minicore.contracts.mvc.MvcConfigurer;
import minicore.contracts.pipeline.IApplicationBuilder;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) throws JsonProcessingException {

        JsonMapper j = new JsonMapper();
        Model m = new Model();
        m.setId(1);
        m.setName("anshu");
        List<Model> mlist= new ArrayList<>();
        mlist.add(m);
        Model m2 =new Model();
        m.setId(2);
        m.setName("priya");
        mlist.add(m2);

        String s=j.writeValueAsString(mlist);

        System.out.println(s);

    }


}
class  Startup implements IStartup{


    @Override
    public void configureServices(IServiceCollection services) {
        MvcConfigurer.configureMvc(services, (option)->{


        });
    }

    @Override
    public void configure(IApplicationBuilder app) {


    }
}
class Model{

    private  int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}