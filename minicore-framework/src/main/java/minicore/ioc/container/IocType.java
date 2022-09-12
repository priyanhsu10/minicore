package minicore.ioc.container;

import java.util.ArrayList;
import java.util.List;
//class is use for identify the cyclic dependency

public class IocType<T extends  Object>{

    public List<Class<?>> Parents= new ArrayList<>();
   public Class<T > source;

    public IocType( Class source,List<Class<?>> parents) {
        if(parents!=null){
            Parents.addAll(parents);
        }
        this.source = source;
    }
    public IocType( Class source) {
        this.source = source;
    }
    public IocType(IocType iocType) {
        if(iocType.Parents!=null){
            Parents.addAll(iocType.Parents);
        }
        source = iocType.source;
    }
    public  boolean checkSourceInParent(){
        return Parents.stream().anyMatch(x->x.equals(source));
    }
}