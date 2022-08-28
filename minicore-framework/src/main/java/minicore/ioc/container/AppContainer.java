package minicore.ioc.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

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
            return source.cast(createInstance(source, descriptor));
        } else if (singletonTank.containsKey(source)) {
            return source.cast(singletonTank.get(source));
        } else if (scopeTank.containsKey(source)) {
            return source.cast(scopeTank.get(source));
        } else {
            throw new RuntimeException("Type Not register in container");
        }


    }

    public Object createInstance(Class source, Descriptor descriptor) {
        boolean isSingleton = descriptor.getScope().equals(Scope.Singleton);
        boolean isScope = descriptor.getScope().equals(Scope.RequestScope);
        if (isScope && scopeTank.containsKey(source)) {
            return scopeTank.get(source);
        }
        if (isSingleton && singletonTank.containsKey(source)) {
            return singletonTank.get(source);
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
            if (isSingleton && !singletonTank.containsKey(source)) {
                singletonTank.put(source, o);
            }
            if (isScope && !scopeTank.containsKey(source)) {
                scopeTank.put(source, o);
            }
            return o;
        }
        Class[] typeParameters = constructor.get().getParameterTypes();
        Object[] parameters = Arrays.stream(typeParameters).map(y -> resolve(y)).toArray();

        try {
            Object o = descriptor.getImplementer()
                    .getConstructor(typeParameters)
                    .newInstance(parameters);
            if (isSingleton && !singletonTank.containsKey(source)) {
                singletonTank.put(source, o);
            }
            if (isScope && !scopeTank.containsKey(source)) {
                scopeTank.put(source, o);
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
