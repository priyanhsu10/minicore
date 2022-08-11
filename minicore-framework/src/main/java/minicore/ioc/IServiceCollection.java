package minicore.ioc;

public interface IServiceCollection {
    <T> T resolve(Class<T> source);
    <TSource>  void addTransient(Class<TSource> source, Class<? extends TSource> target);
    <TSource>  void addSingleton(Class<TSource> source, Class<? extends TSource> target);
    <TSource>  void AddScope(Class<TSource> source, Class<? extends TSource> target);
    <TSource> void register(Class<TSource> source, Scope scope);

}

