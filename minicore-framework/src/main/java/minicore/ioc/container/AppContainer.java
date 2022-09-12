package minicore.ioc.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

public class AppContainer {
    public final HashMap<Class, Descriptor> tank = new HashMap<>();
    public HashMap<Class, Object> singletonTank = new HashMap<>();
    // this hashMap will be clean at every request end
    public HashMap<Class, Object> scopeTank = new HashMap<>();

    public <Source> void addTransient(Class<Source> source, Class<? extends Source> target) {
        validate(target);
        register(new Descriptor(source, target, Scope.Transient));
    }

    public <Source> void addSingleton(Class<Source> source, Class<? extends Source> target) {
        validate(target);
        register(new Descriptor(source, target, Scope.Singleton));
    }

    private void validate(Class target) {
        if (Modifier.isAbstract(target.getModifiers())) {
            throw new RuntimeException("Invalid type to register :" + target.getName());
        }
    }

    public <Source> void addScope(Class<Source> source, Class<? extends Source> target) {
        validate(target);
        register(new Descriptor(source, target, Scope.RequestScope));
    }

    public void register(Descriptor descriptor) {
        validate(descriptor.getImplementer());
        tank.put(descriptor.getSource(), descriptor);
    }

    public <T> T resolve(Class<T> source) {
        if (tank.containsKey(source)) {
            Descriptor descriptor = tank.get(source);
            return source.cast(createInstance(new IocType(source), descriptor));
        } else if (singletonTank.containsKey(source)) {
            return source.cast(singletonTank.get(source));
        } else if (scopeTank.containsKey(source)) {
            return source.cast(scopeTank.get(source));
        } else {
            throw new RuntimeException("Type Not register in container");
        }


    }
    private  Object resolveInternal(IocType iocType) {

        if (tank.containsKey(iocType.source)) {
            Descriptor descriptor = tank.get(iocType.source);
            if(iocType.checkSourceInParent()){
            String smessage=   getCyclicDependencyMessage(iocType.Parents,descriptor.getImplementer());
                String message= "Cyclic dependency identified while resolving :" +iocType.source.toString() ;
                message+="\n"+smessage;
                System.out.println(message);
                throw  new RuntimeException(message);
            }

            return iocType.source.cast(createInstance(iocType, descriptor));
        } else if (singletonTank.containsKey(iocType.source)) {
            return iocType.source.cast(singletonTank.get(iocType.source));
        } else if (scopeTank.containsKey(iocType.source)) {
            return iocType.source.cast(scopeTank.get(iocType.source));
        } else {
            throw new RuntimeException("Type Not register in container");
        }
    }

    private String getCyclicDependencyMessage(List<Class<?>> visited, Class<?> implementer) {

        String dasshes="-------->";

        String  result="";
        int counter=1;

        for (Class<?>c:visited) {
            result+= c.getName()+"\n" ;
            result+=dasshes+"\n" ;
           result+=addspace(counter);
           counter++;
        }
        //A
        // ----->
        //        B------->
        //                   C
        result+="\n"+implementer.getName();
        return result;
    }

    private String addspace(int counter) {
        String s="";
        String space="        ";
        for(int i=1;i<=counter;i++){
            s+=space;
        }
        return s +"\n" ;
    }

    //try to resove if type is not register then it register and then resolve
    public <T> T tryResolve(Class<T> source ,Scope scope) {
        if (tank.containsKey(source)) {
            Descriptor descriptor = tank.get(source);
            List<Class<?>> Visited=new ArrayList<>();
            return source.cast(createInstance(new IocType(source), descriptor));
        } else if (singletonTank.containsKey(source)) {
            return source.cast(singletonTank.get(source));
        } else if (scopeTank.containsKey(source)) {
            return source.cast(scopeTank.get(source));
        } else {
            register(new Descriptor(source,source,scope));
           return resolve(source);
        }


    }

    public Object createInstance(IocType iocType, Descriptor descriptor) {
        boolean isSingleton = descriptor.getScope().equals(Scope.Singleton);
        boolean isScope = descriptor.getScope().equals(Scope.RequestScope);
        if (isScope && scopeTank.containsKey(iocType.source)) {
            return scopeTank.get(iocType.source);
        }
        if (isSingleton && singletonTank.containsKey(iocType.source)) {
            return singletonTank.get(iocType.source);
        }
        Optional<Constructor<?>> constructor = Arrays.stream(descriptor.getImplementer().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount));

        if (!constructor.isPresent()) {
            Object o = null;
            try {
                o = descriptor.getImplementer().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (isSingleton && !singletonTank.containsKey(iocType.source)) {
                singletonTank.put(iocType.source, o);
            }
            if (isScope && !scopeTank.containsKey(iocType.source)) {
                scopeTank.put(iocType.source, o);
            }
            return o;
        }
        Class[] typeParameters = constructor.get().getParameterTypes();
        Object[] parameters = Arrays.stream(typeParameters).map(y -> {
                    List<Class<?>> parents =new ArrayList<>();
                    parents.addAll(iocType.Parents);
                    parents.add(iocType.source);
                    return resolveInternal(new IocType(y, parents));
                }
            ).toArray();
        try {
            Object o = descriptor.getImplementer()
                    .getConstructor(typeParameters)
                    .newInstance(parameters);
            if (isSingleton && !singletonTank.containsKey(iocType.source)) {
                singletonTank.put(iocType.source, o);
            }
            if (isScope && !scopeTank.containsKey(iocType.source)) {
                scopeTank.put(iocType.source, o);
            }
            return o;
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }


    }

    public void clearSchopeObjects() {

        scopeTank.clear();
        scopeTank = new HashMap<>();
    }

    @Override
    public void finalize() throws Throwable {
        scopeTank.clear();
        scopeTank = null;
        singletonTank.clear();
        singletonTank = null;

    }
}
