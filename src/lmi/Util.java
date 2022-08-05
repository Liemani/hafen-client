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

    static boolean isClassType(Object object) {
        if (object == null)
            return false;
        else
            return object.getClass() == Class.class;
    }

    // return if array has same address of element
    static <T> boolean contains(T array[], T element) {
        for (T entry : array)
            if (entry == element)
                return true;
        return false;
    }

    static Object getFieldValueFromObjectByNameAsClass(Object object, String name, Class classObject) throws Exception {
        while (classObject != Object.class) {
            try {
                return classObject
                    .getDeclaredField(name)
                    .get(object);
            } catch (Exception e) {}
            classObject = classObject.getSuperclass();
        }
        return null;
    }

    static void insertIndent(int indentCount) {
        while (--indentCount >= 0)
            System.out.print("  ");
    }
}
