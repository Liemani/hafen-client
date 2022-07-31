package lmi.debug;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.lang.Number;
import java.io.PrintStream;

import lmi.Util;

public class Debug {
    // primitive category(not practical use)
    public static final String OPEN_BRACKETS   = "[{";
    public static final String CLOSE_BRACKETS  = "]}";
    public static final String COMMAS          = ",";

    // practical category(practical use)
    public static String openBrackets = OPEN_BRACKETS;
    public static String closeBrackets = CLOSE_BRACKETS;
    public static String commas = COMMAS;
    public static String newLineFollowChars = OPEN_BRACKETS + COMMAS;

    // public methods
    public static void debugDescribe(PrintStream printStream, Object object) {
        debugDescribe(printStream, object, 2);
    }

    public static void debugDescribe(PrintStream printStream, Object object, final int indentSpace) {
        String rawDescription;
        if (object instanceof Class)
            rawDescription = debugDescription((Class)object);
        else
            rawDescription = debugDescription(object);
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

    public static void debugDescribeVariable(String identifier, Object object) {
        StringBuilder description = new StringBuilder();

        description.append("{");
        description.append("\"" + identifier + "\"" + ":");

        String primitiveDescription = primitiveTypeDescription(object);
        if (primitiveDescription != null)
            description.append(primitiveDescription);
        else
            description.append(object);

        description.append("}");

        System.out.println(description.toString());
    }

    // private methods
    private static String primitiveTypeDescription(Object object) {
        if (object == null) {
            return "\"null\"";
        }
        else if (object instanceof Number) {
            return object.toString();
        }
        else if (object instanceof String) {
            return "\"" + (String)object + "\"";
        }
        return null;
    }

    private static String debugDescription(Object object) {
        if (!Util.isClass(object)) {
            String primitiveDescription = primitiveTypeDescription(object);
            if (primitiveDescription != null)
                return primitiveDescription;
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
                String primitiveDescription = primitiveTypeDescription(fieldObject);
                if (primitiveDescription != null)
                    description.append(primitiveDescription);
                else
                    description.append(fieldObject);
            } catch (Exception e) {
                description.append("\"<access denied>\"");
                System.out.println(e.getMessage());
            }
            description.append(",");
        }

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
        debugDescribe(System.out, lmi.debug.Debug.class);
        debugDescribe(System.out, new Object());
    }
}
