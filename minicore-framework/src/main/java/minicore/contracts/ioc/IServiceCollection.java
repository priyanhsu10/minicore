package minicore.contracts.ioc;

import minicore.ioc.container.Scope;

public interface IServiceCollection {
    <T> T resolve(Class<T> source);
    <TSource>  void addTransient(Class<TSource> source, Class<? extends TSource> target);
    <TSource>  void addSingleton(Class<TSource> source, Class<? extends TSource> target);
    <TSource>  void addScope(Class<TSource> source, Class<? extends TSource> target);
    <TSource> void register(Class<TSource> source, Scope scope);
    <TSource> void addSingleton(Class<TSource> source,IResolveInstance<? extends TSource> resolve);
    default  void clearRequestObjects(){
        //implement by child
    }

}
