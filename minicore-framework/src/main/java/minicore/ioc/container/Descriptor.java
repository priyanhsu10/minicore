package minicore.ioc.container;

public class Descriptor {
    private Class<? extends Object> source;
    private Class<? extends Object> implementer;
    private Scope scope;

    public Class<?> getSource() {
        return source;
    }

    public Class<?> getImplementer() {
        return implementer;
    }

    public Scope getScope() {
        return scope;
    }

    public Descriptor(Class<? extends Object> source, Class<? extends Object> implementer) {
        this.source = source;
        this.implementer = implementer;
        this.scope = Scope.Singleton;
    }

    public Descriptor(Class<? extends Object> source, Class<? extends Object> implementer, Scope scope) {
        this.source = source;
        this.implementer = implementer;
        this.scope = scope;
    }
}
