package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import haven.Gob;
import haven.Coord;

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

    // etc
    public static String targetStack() {
        StackTraceElement targetStack = new Throwable().getStackTrace()[2];
        return targetStack.getClassName() + "::" + targetStack.getMethodName();
    }

    public static void debugPrint(String description) {
        final StringBuilder fullDescription = new StringBuilder();

        fullDescription.append("[" + Util.targetStack() + "()]");
        if (description != null)
            fullDescription.append(" { " + description + " }");

        System.out.println(fullDescription.toString());
    }

    public static void debugPrint() {
        System.out.println("[" + Util.targetStack() + "()]");
    }

    public static int stoi(String string) { return Integer.parseInt(string); }

    // TODO 화면 사이즈가 바뀌면 이 값도 바꿔주도록 하자
    // 아마 frame에 sizeChanged() 같은 event가 있을 것 같다
    private static Coord mapViewCenter_;

    public static Coord mapViewCenter() {
        return Util.mapViewCenter_;
    }

    public static void initMapViewCenterByMapView(haven.MapView mapView) {
        mapViewCenter_ = mapView.sz.div(2);
    }

    // TODO re-implement
    public static void waitHourGlassFailable() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final long timeoutLimit = startTime + TO_GENERAL;
        while (lmi.ObjectShadow.gameUI().prog == null) {
            Thread.sleep(TO_TEMPORARY);
            long currentTime = System.currentTimeMillis();
            if (currentTime > timeoutLimit)
                break;
        }
        while (lmi.ObjectShadow.gameUI().prog != null)
            Thread.sleep(TO_TEMPORARY);
    }

    public static void newWidget(haven.Widget widget) {
        if (widget.getClass() == haven.FlowerMenu.class) {
            FlowerMenuHandler.setWidget((haven.FlowerMenu)widget);
        }
    }

//      iter = oc.iterator();
//      while(iter.hasNext()) {
//          System.out.println(iter.next());
//      }
    public static java.util.Iterator<Gob> iterator() { return ObjectShadow.objectCache().iterator(); }

    // find gob
    public static Gob closestGob() {
        java.util.Iterator<Gob> iterator;
        Gob gob = null;
        while (true) {
            try {
                iterator = Util.iterator();
                gob = closestGob(iterator);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return gob;
    }

    private static Gob closestGob(java.util.Iterator<Gob> iterator) {
        Gob gob;
        double distance;

        Gob closestGob = null;
        double closestDistance = 1100.0;

        while (iterator.hasNext()) {
            gob = iterator.next();
            if (gob.getClass() != Gob.class)
                continue;
            if (Self.gob().isAt(gob.location()))
                continue;
            distance = Self.gob().distance(gob);
            if (distance < closestDistance) {
                closestGob = gob;
                closestDistance = distance;
            }
        }

        return closestGob;
    }
}
