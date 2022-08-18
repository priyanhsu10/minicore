package minicore.ioc;

import minicore.ioc.container.AppContainer;
import minicore.ioc.container.Descriptor;
import minicore.ioc.container.Scope;

public class ServiceCollection implements IServiceCollection {
    private final AppContainer container;

    public AppContainer getContainer() {
        return container;
    }

    public ServiceCollection() {
        container = new AppContainer();

    }

    @Override
    public <TSource> void addTransient(Class<TSource> source, Class<? extends TSource> target) {
        container.addTransient(source, target);
    }

    @Override
    public <TSource> void addSingleton(Class<TSource> source, Class<? extends TSource> target) {
        container.addSingleton(source, target);
    }

    @Override
    public <TSource> void AddScope(Class<TSource> source, Class<? extends TSource> target) {
        container.addScope(source, target);
    }

    public <T> T resolve(Class<T> source) {
        return container.resolve(source);
    }


    @Override
    public <TSource> void register(Class<TSource> source, Scope scope) {
        container.register(new Descriptor(source,source,scope));
    }
}