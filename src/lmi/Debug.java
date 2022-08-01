package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.lang.Number;
import java.io.PrintStream;

import lmi.Util;

public class Debug {
    // primitive category(not practical use)
    static final String OPEN_BRACKETS   = "[{";
    static final String CLOSE_BRACKETS  = "]}";
    static final String COMMAS          = ",";

    // practical category(practical use)
    static String openBrackets = OPEN_BRACKETS;
    static String closeBrackets = CLOSE_BRACKETS;
    static String commas = COMMAS;
    static String newLineFollowChars = OPEN_BRACKETS + COMMAS;

    // public methods
    public static void debugDescribe(Object object) {
        debugDescribe(System.out, object);
    }

    public static void debugDescribe(PrintStream printStream, Object object) {
        if (object.getClass() == String.class)
            debugDescribe(printStream, (String)object, 2);
        else
            debugDescribe(printStream, object, 2);
    }

    public static void debugDescribe(PrintStream printStream, Object object, final int indentSpace) {
        String rawDescription;

        if (object instanceof Class)
            rawDescription = debugDescription((Class)object);
        else
            rawDescription = debugDescription(object);
        debugDescribe(printStream, rawDescription, indentSpace);
    }

    public static void debugDescribe(PrintStream printStream, String rawDescription, final int indentSpace) {
        char charArray[] = rawDescription.toCharArray();

        StringBuilder description = new StringBuilder();

        int indentDepth = 0;
        boolean isInString = false;
        char previousChar = '\0';

        for (char currentChar : charArray) {
            if (isInString) {
                description.append(currentChar);
                if (currentChar == '\"') {
                    isInString = false;
                    previousChar = currentChar;
                }
                continue;
            }

            switch (currentChar) {
                case '{':
                case '[':
                    if (newLineFollowChars.indexOf(previousChar) != -1) {
                        description.append('\n');
                        printIndent(description, indentDepth, indentSpace);
                    }
                    description.append(currentChar);
                    ++indentDepth;
                    previousChar = currentChar;
                    break;
                case '}':
                case ']':
                    --indentDepth;
                    if (openBrackets.indexOf(previousChar) == -1) {
                        description.append('\n');
                        printIndent(description, indentDepth, indentSpace);
                    }
                    description.append(currentChar);
                    previousChar = currentChar;
                    break;
                case ':':
                    description.append(" : ");
                    previousChar = currentChar;
                    break;
                case ',':
                    description.append(',');
                    previousChar = currentChar;
                    break;
                case '\t':
                case '\n':
                case ' ':
                    break;
                case '\"':
                    if (newLineFollowChars.indexOf(previousChar) != -1) {
                        description.append('\n');
                        printIndent(description, indentDepth, indentSpace);
                    }
                    description.append(currentChar);
                    isInString = true;
                    break;
                default:
                    if (newLineFollowChars.indexOf(previousChar) != -1) {
                        description.append('\n');
                        printIndent(description, indentDepth, indentSpace);
                    }
                    description.append(currentChar);
                    previousChar = currentChar;
                    break;
            }
        }

        printStream.println(description.toString());
    }

    // private methods
    private static String debugDescription(Object object) {
        if (!Util.isClass(object)) {
            String descriptionOfSpecialType = debugDescriptionSpecialType(object);
            if (descriptionOfSpecialType != null)
                return descriptionOfSpecialType;
        }

        final Class classObject = Util.isClass(object) ? (Class)object : object.getClass();

        StringBuilder description = new StringBuilder();
        description.append("{");
        description.append("\"class name\"" + ":" + "\"" + classObject.getName() + "\"" + ",");
        description.append("\"fields\"" + ":");
        description.append(debugDescriptionFeilds(object, classObject));
        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionFeilds(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        description.append("{");

        while (classObject != null) {
            description.append(debugDescriptionFeildsAsClass(object, classObject));
            classObject = (Class)classObject.getGenericSuperclass();
        }

        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionFeildsAsClass(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        Field[] fields = classObject.getDeclaredFields();

        for (Field field : fields) {
            if (Util.isClass(object) && !Util.fieldHasModifier(field, Modifier.STATIC)
                    || !Util.isClass(object) && Util.fieldHasModifier(field, Modifier.STATIC))
                continue;

            description.append("\"" + field.getName() + "\"" + ":");
            try {
                final Object fieldObject = field.get(object);
                String descriptionOfSpecialType = debugDescriptionSpecialType(fieldObject);
                if (descriptionOfSpecialType != null)
                    description.append(descriptionOfSpecialType);
                else
                    description.append(fieldObject);
            } catch (Exception e) {
                description.append("\"<access denied>\"");
                if (e.getClass() == java.lang.IllegalAccessException.class)
                    System.out.println(e.getMessage());
                else
                    e.printStackTrace();
            }
            description.append(",");
        }

        return description.toString();
    }

    private static String debugDescriptionSpecialType(Object object) {
        if (object == null)
            return "\"null\"";

        Class classOfObject = object.getClass();

        if (classOfObject == String.class) {
            return "\"" + (String)object + "\"";
        }
        else if (classOfObject == Number.class) {
            return object.toString();
        }
        else if (classOfObject.isArray()) {
            return debugDescriptionSpecialTypeArray(object);
        }

        return null;
    }

    private static String debugDescriptionSpecialTypeArray(Object array) {
        if (array.getClass() == byte[].class)
            return debugDescriptionSpecialTypeByteArray((byte[])array);
        else if (array.getClass() == int[].class)
            return debugDescriptionSpecialTypeIntArray((int[])array);
        else if (array.getClass() == double[].class)
            return debugDescriptionSpecialTypeDoubleArray((double[])array);
        else
            return debugDescriptionSpecialTypeObjectArray((Object[])array);
    }

    private static String debugDescriptionSpecialTypeByteArray(byte byteArray[]) {
        StringBuilder description = new StringBuilder();

        description.append("[");
        for (byte element : byteArray) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    private static String debugDescriptionSpecialTypeIntArray(int intArray[]) {
        StringBuilder description = new StringBuilder();

        description.append("[");
        for (int element : intArray) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    private static String debugDescriptionSpecialTypeDoubleArray(double doubleArray[]) {
        StringBuilder description = new StringBuilder();

        description.append("[");
        for (double element : doubleArray) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    private static String debugDescriptionSpecialTypeObjectArray(Object objectArray[]) {
        StringBuilder description = new StringBuilder();

        description.append("[");
        for (Object element : objectArray) {
            description.append(element.toString());
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    private static void printIndent(StringBuilder description, int indentDepth, final int indentSpace) {
        for (int i = 0; i < indentDepth; ++i)
            for (int j = 0; j < indentSpace; ++j)
                description.append(' ');
    }

    // main function
    public static void main(String args[]) {
        Debug debug = new Debug();

        System.out.println(debugDescription(debug));
        debugDescribe(System.out, debug);
        debugDescribe(System.out, null);
        debugDescribe(System.out, Debug.class);
        debugDescribe(System.out, new Object());

//          Debug[] debugArray = { new Debug(), new Debug() };
//          debugDescribe(System.out, debugArray);

        int[] intArray = { 1, 2, 3 };
        debugDescribe(System.out, intArray);
    }
}
