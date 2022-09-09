package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Predicate;

import haven.Gob;
import haven.Coord;

import static lmi.Constant.ExceptionType.*;
import static lmi.Constant.TimeOut.*;

public class Util {
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

    // string functions
    public static String convertToString(byte[] array, int offset) {
        int stringLength = Util.strlen(array, offset);
        return new String(array, offset, stringLength);
    }

    public static int strlen(byte[] array, int stringOffset) {
        int stringEndOffset = stringOffset;
        while (stringEndOffset < array.length && array[stringEndOffset] != 0)
            ++stringEndOffset;
        return stringEndOffset - stringOffset;
    }

    public static int stoi(String string) { return Integer.parseInt(string); }

    // etc
    public static String targetStack() {
        StackTraceElement targetStack = new Throwable().getStackTrace()[2];
        return targetStack.getClassName() + "::" + targetStack.getMethodName();
    }

    // Debug Print
    public static void debugPrint(String description) {
        final StringBuilder fullDescription = new StringBuilder();

        fullDescription.append("[" + Util.targetStack() + "()]");
        if (description != null)
            fullDescription.append(" { " + description + " }");

        System.out.println(fullDescription.toString());
    }

    public static void debugPrint(Object object) {
        Util.debugPrint(object.toString());
    }

    public static void debugPrint() {
        System.out.println("[" + Util.targetStack() + "()]");
    }

    // TODO 화면 사이즈가 바뀌면 이 값도 바꿔주도록 하자
    // 아마 frame에 sizeChanged() 같은 event가 있을 것 같다
    private static Coord _mapViewCenter;
    public static Coord mapViewCenter() { return _mapViewCenter; }

    public static void initMapViewCenterByMapView(haven.MapView mapView) {
        _mapViewCenter = mapView.sz.div(2);
    }

    public static void sleep(long microseconds) {
        try {
            Thread.sleep(microseconds);
        } catch (InterruptedException e) {
            throw new LMIException(ET_INTERRUPTED);
        }
    }

    // print GameUI message
    public static void printErrorMessage(String message) {
        ObjectShadow.gameUI().error(message);
    }

    public static void printAlertMessage(String message) {
        ObjectShadow.gameUI().alert(message);
    }
}
