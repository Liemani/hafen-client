package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.lang.Number;

import java.io.PrintStream;

import java.util.ArrayList;

import lmi.Util;

public class Debug {
    static class Context {
        // closer type definition
        static interface Symbol { }

        static class Comma implements Symbol { }

        static class Colon implements Symbol { }

        static interface Encloser extends Symbol {
            static boolean isOpenChar(char ch) {
                return Bracket.isOpenChar(ch)
                    || Quote.isOpenChar(ch);
            }

            static boolean isCloseChar(char ch) {
                return Bracket.isCloseChar(ch)
                    || Quote.isCloseChar(ch);
            }
        }

        static interface Bracket extends Encloser {
            static boolean isOpenChar(char ch) {
                return Parenthesis.isOpenChar(ch)
                    || CurlyBrace.isOpenChar(ch)
                    || SquareBracket.isOpenChar(ch);
            }

            static boolean isCloseChar(char ch) {
                return Parenthesis.isCloseChar(ch)
                    || CurlyBrace.isCloseChar(ch)
                    || SquareBracket.isCloseChar(ch);
            }
        }

        static class Parenthesis implements Bracket {
            static boolean isOpenChar(char ch) { return ch == '('; }
            static boolean isCloseChar(char ch) { return ch == ')'; }
        }

        static class CurlyBrace implements Bracket {
            static boolean isOpenChar(char ch) { return ch == '{'; }
            static boolean isCloseChar(char ch) { return ch == '}'; }
        }

        static class SquareBracket implements Bracket {
            static boolean isOpenChar(char ch) { return ch == '['; }
            static boolean isCloseChar(char ch) { return ch == ']'; }
        }

        interface Quote extends Encloser {
            static boolean isOpenChar(char ch) {
                return SingleQuote.isOpenChar(ch)
                    || DoubleQuote.isOpenChar(ch);
            }

            static boolean isCloseChar(char ch) {
                return SingleQuote.isCloseChar(ch)
                    || DoubleQuote.isCloseChar(ch);
            }
        }

        static class SingleQuote implements Quote {
            static boolean isOpenChar(char ch) { return ch == '\''; }
            static boolean isCloseChar(char ch) { return isOpenChar(ch); }
        }

        static class DoubleQuote implements Quote {
            static boolean isOpenChar(char ch) { return ch == '\"'; }
            static boolean isCloseChar(char ch) { return isOpenChar(ch); }
        }

        // fields
        int indentCount_ = 0;
        char previousNotSpaceChar_ = '\0';
        ArrayList<Class> encloserStack_ = new ArrayList<Class>();

        void adjustIndentBeforeUse(char ch) {
            if (CurlyBrace.isCloseChar(ch))
                --indentCount_;
        }

        void adjustIndentAfterUse(char ch) {
            if (CurlyBrace.isOpenChar(ch))
                ++indentCount_;
        }

        // package methods
        void setPreviousNotSpaceChar(char ch) {
            Class encloserType;

            if ((encloserType = getEncloserClassCloseChar(ch)) != null
                    && encloserType == lastEncloser())
                popEncloserStack();
            else if ((encloserType = getEncloserClassOpenChar(ch)) != null)
                pushEncloserStack(encloserType);

            previousNotSpaceChar_ = ch;
        }

        boolean isInDoubleQuotes() {
            return lastEncloser() == DoubleQuote.class;
        }

        boolean isInCurlyBraces() {
            return lastEncloser() == CurlyBrace.class;
        }

        // {_[^}]
        // [^{]_}
        // ,_[^{] (in {})
        boolean shouldPutNewLine(char ch) {
            if (CurlyBrace.isOpenChar(previousNotSpaceChar_) && !CurlyBrace.isCloseChar(ch)
                    || !CurlyBrace.isOpenChar(previousNotSpaceChar_) && CurlyBrace.isCloseChar(ch)
                    || previousNotSpaceChar_ == ',' && !CurlyBrace.isOpenChar(ch) && isInCurlyBraces())
                return true;
            else
                return false;
        }

        // ,_. (in ())
        // ,_{ (in {})
        // ,_. (in [])
        // :_.
        // ._:
        boolean shouldPutSpace(char ch) {
            Class currentEncloser = lastEncloser();
            if (previousNotSpaceChar_ == ','
                        && (currentEncloser == Parenthesis.class
                            || ch == '{' && currentEncloser == CurlyBrace.class
                            || currentEncloser == SquareBracket.class)
                    || previousNotSpaceChar_ == ':'
                    || ch == ':')
                return true;
            else
                return false;
        }

        // private methods
        private Class getEncloserClassOpenChar(char ch) {
            if (Parenthesis.isOpenChar(ch))
                return Parenthesis.class;
            else if (CurlyBrace.isOpenChar(ch))
                return CurlyBrace.class;
            else if (SquareBracket.isOpenChar(ch))
                return SquareBracket.class;
            else if (SingleQuote.isOpenChar(ch))
                return SingleQuote.class;
            else if (DoubleQuote.isOpenChar(ch))
                return DoubleQuote.class;
            else
                return null;
        }

        private Class getEncloserClassCloseChar(char ch) {
            if (Parenthesis.isCloseChar(ch))
                return Parenthesis.class;
            else if (CurlyBrace.isCloseChar(ch))
                return CurlyBrace.class;
            else if (SquareBracket.isCloseChar(ch))
                return SquareBracket.class;
            else if (SingleQuote.isCloseChar(ch))
                return SingleQuote.class;
            else if (DoubleQuote.isCloseChar(ch))
                return DoubleQuote.class;
            else
                return null;
        }

        private void pushEncloserStack(Class encloserClass) {
            encloserStack_.add(encloserClass);
        }

        private Class popEncloserStack() {
            int size = encloserStack_.size();

            if (size == 0)
                return null;
            else
            return encloserStack_.remove(encloserStack_.size() - 1);
        }

        private Class lastEncloser() {
            int size = encloserStack_.size();

            if (size == 0)
                return null;
            else
                return encloserStack_.get(size - 1);
        }
    }

    // public methods
    public static void debugDescribe(Object object) {
        debugDescribe(System.out, object);
    }

    public static void debugDescribe(PrintStream printStream, Object object) {
        if (object != null && object.getClass() == String.class)
            debugDescribe(printStream, (String)object, 2);
        else
            debugDescribe(printStream, object, 2);
    }

    public static void debugDescribe(PrintStream printStream, Object object, final int indentSize) {
        String rawDescription;

        if (object != null && Util.isClass(object))
            rawDescription = debugDescription((Class)object);
        else
            rawDescription = debugDescription(object);

        debugDescribe(printStream, rawDescription, indentSize);
    }

    public static void debugDescribeMethod(Object object) {
        debugDescribeMethod(System.out, object);
    }

    public static void debugDescribeMethod(PrintStream printStream, Object object) {
        debugDescribeMethod(printStream, object, 2);
    }

    public static void debugDescribeMethod(PrintStream printStream, Object object, final int indentSize) {
        String rawDescription;

        if (object == null) {
            rawDescription = "null";
        }
        else if (Util.isClass(object))
            rawDescription = debugDescriptionMethods((Class)object);
        else
            rawDescription = debugDescriptionMethods(object);

        debugDescribe(printStream, rawDescription, indentSize);
    }

    public static void debugDescribe(PrintStream printStream, String rawDescription, final int indentSize) {
        Context context = new Context();

        char descriptionAsCharArray[] = rawDescription.toCharArray();
        StringBuilder description = new StringBuilder();
        for (char ch : descriptionAsCharArray) {
            if (context.isInDoubleQuotes()) {
                description.append(ch);
                if (Context.DoubleQuote.isCloseChar(ch))
                    context.setPreviousNotSpaceChar(ch);
                continue;
            }

            context.adjustIndentBeforeUse(ch);

            if (context.shouldPutNewLine(ch)) {
                description.append('\n');
                appendIndent(description, context.indentCount_, indentSize);
            }

            context.adjustIndentAfterUse(ch);

            if (context.shouldPutSpace(ch))
                description.append(' ');

            switch (ch) {
                case '\t':
                case '\n':
                case ' ':
                    break;
                default:
                    description.append(ch);
                    context.setPreviousNotSpaceChar(ch);
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
        description.append(debugDescriptionFields(object, classObject));
        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionFields(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        description.append("{");

        while (classObject != null) {
            description.append(debugDescriptionFieldsAsClass(object, classObject));
            classObject = (Class)classObject.getGenericSuperclass();
        }

        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionFieldsAsClass(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        Field[] fields = classObject.getDeclaredFields();

        for (Field field : fields) {
            if (Util.isClass(object) && !Util.fieldHasModifier(field, Modifier.STATIC)
                    || !Util.isClass(object) && Util.fieldHasModifier(field, Modifier.STATIC))
                continue;

            description.append("{");
            description.append("\"name\"" + ":");
            description.append("\"" + field.getName() + "\"");
            description.append(",");
            description.append("\"type\"" + ":");
            description.append("\"" + field.getGenericType().getTypeName() + "\"");
            description.append(",");
            description.append("\"value\"" + ":");
            try {
                final Object fieldObject = field.get(object);
                String descriptionOfSpecialType = debugDescriptionSpecialType(fieldObject);
                if (descriptionOfSpecialType != null)
                    description.append(descriptionOfSpecialType);
                else
                    description.append("\"" + fieldObject + "\"");
            } catch (Exception e) {
                description.append("\"<access denied>\"");
                if (e.getClass() == java.lang.IllegalAccessException.class)
                    System.out.println(e.getMessage());
                else
                    e.printStackTrace();
            }
            description.append("}");
            description.append(",");
        }

        return description.toString();
    }

    private static String debugDescriptionSpecialType(Object object) {
        if (object == null)
            return "\"null\"";

        Class classOfObject = object.getClass();

        if (classOfObject == Boolean.class) {
            return (Boolean)object ? "true" : "false";
        }
        else if (object instanceof Number) {
            return object.toString();
        }
        else if (classOfObject == String.class) {
            return "\"" + (String)object + "\"";
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

    private static String debugDescriptionMethods(Object object) {
        final Class classObject = Util.isClass(object) ? (Class)object : object.getClass();

        StringBuilder description = new StringBuilder();
        description.append("{");
        description.append("\"class name\"" + ":" + "\"" + classObject.getName() + "\"" + ",");
        description.append("\"methods\"" + ":");
        description.append(debugDescriptionMethods(object, classObject));
        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionMethods(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        description.append("{");

        while (classObject != null) {
            description.append(debugDescriptionMethodsAsClass(object, classObject));
            classObject = (Class)classObject.getGenericSuperclass();
        }

        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionMethodsAsClass(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        Method[] methods = classObject.getDeclaredMethods();

        for (Method method : methods) {
            if (Util.isClass(object) && !Util.methodHasModifier(method, Modifier.STATIC)
                    || !Util.isClass(object) && Util.methodHasModifier(method, Modifier.STATIC))
                continue;

            description.append("\"" + method.toGenericString() + "\"");
            description.append(",");
        }

        return description.toString();
    }

    private static void appendIndent(StringBuilder description, int indentDepth, final int indentSize) {
        for (int i = 0; i < indentDepth; ++i)
            for (int j = 0; j < indentSize; ++j)
                description.append(' ');
    }

    // main function
    public static void main(String args[]) {
        Debug debug = new Debug();

//          System.out.println(debugDescription(debug));
        debugDescribe(debug);
//          debugDescribe(System.out, null);
//          debugDescribe(System.out, Debug.class);
//          debugDescribe(System.out, new Object());
//  
//  //          Debug[] debugArray = { new Debug(), new Debug() };
//  //          debugDescribe(System.out, debugArray);
//  
//          int[] intArray = { 1, 2, 3 };
//          debugDescribe(System.out, intArray);
//          haven.UIPanel.Dispatcher dispatcher = new haven.UIPanel.Dispatcher();
//          dispatcher
//          debugDescribeMethod(Debug.class);
    }
}
