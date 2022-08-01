package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Util {
    static boolean fieldHasModifier(Field field, int modifier) {
        return (field.getModifiers() & modifier) != 0;
    }

    static boolean methodHasModifier(Method method, int modifier) {
        return (method.getModifiers() & modifier) != 0;
    }

    static boolean isClass(Object object) {
        return object instanceof Class;
    }
}
