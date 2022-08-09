package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class Util {
    enum MemberType {
        FIELD,
        METHOD;

        boolean isField() {
            return this == FIELD;
        }

        boolean isMethod() {
            return this == METHOD;
        }
    }

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

    // resourceName()
    static String resourceName(haven.IMeter gauge) {
        return gauge.bg.get().name;
    }

    // return if array has same address of element
    static <T> boolean contains(T array[], T element) {
        for (T entry : array)
            if (entry == element)
                return true;
        return false;
    }

    // getObjectByInputFromProperty()
    static Object getObjectByInputFromField(Object object, Class classObject) throws Exception {
        printPublicFieldList(classObject);
        String fieldName = lmi.Scanner.nextLineWithPrompt("enter field name");
        object = Util.getFieldValueFromObjectByNameAsClass(object, fieldName, classObject);

        return object;
    }

    static Object getObjectByInputFromMethod(Object object, Class classObject) throws Exception {
        printPublicMethodList(classObject);
        String methodName = lmi.Scanner.nextLineWithPrompt("enter method name");
        object = Util.getMethodValueFromObjectByNameAsClass(object, methodName, classObject);

        return object;
    }

    // printPropertyList()
    private static void printPublicFieldList(Class classObject) {
        System.out.println("field list:");
        for (; classObject != Object.class; classObject = classObject.getSuperclass()) {
            Field[] fields = classObject.getDeclaredFields();
            for (Field field : fields) {
                if (Util.fieldHasModifier(field, Modifier.PUBLIC)) {
                    System.out.print("  ");
                    System.out.println(field.getName());
                }
            }
        }
    }

    private static void printPublicMethodList(Class classObject) {
        System.out.println("method list:");
        for (; classObject != Object.class; classObject = classObject.getSuperclass()) {
            Method[] methods = classObject.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getReturnType() != Void.class
                        && method.getParameterCount() == 0
                        && Util.methodHasModifier(method, Modifier.PUBLIC)) {
                    System.out.print("  ");
                    System.out.println(method.getName());
                }
            }
        }
    }

    // getPropertyValueFromObjectByNameAsClass()
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

    static Object getMethodValueFromObjectByNameAsClass(Object object, String name, Class<?> classObject) throws Exception {
        while (classObject != Object.class) {
            try {
                return classObject
                    .getDeclaredMethod(name)
                    .invoke(object);
            } catch (Exception e) {}
            classObject = classObject.getSuperclass();
        }
        return null;
    }

    // insertIndent()
    static void insertIndent(int indentCount) {
        while (--indentCount >= 0)
            System.out.print("  ");
    }
}
