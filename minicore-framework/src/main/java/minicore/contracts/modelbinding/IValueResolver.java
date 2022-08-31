package minicore.contracts.modelbinding;

import java.lang.reflect.Parameter;

public interface IValueResolver {

    Object resolve(Parameter p);
}
