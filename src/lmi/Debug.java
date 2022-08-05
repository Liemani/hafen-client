// Integer type of object's field would be described as like primitive type
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
        char previousNotSpaceChar_ = '\000';
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
        //
        // second version
        // ,_["{] (when indent <= 2)
        boolean shouldPutNewLine(char ch) {
//              if (CurlyBrace.isOpenChar(previousNotSpaceChar_) && !CurlyBrace.isCloseChar(ch)
//                      || !CurlyBrace.isOpenChar(previousNotSpaceChar_) && CurlyBrace.isCloseChar(ch)
//                      || previousNotSpaceChar_ == ',' && !CurlyBrace.isOpenChar(ch) && isInCurlyBraces())
            if (previousNotSpaceChar_ == ',' && (ch == '\"' || ch == '{') && indentCount_ <= 2)
                return true;
            else
                return false;
        }

        // not in ""
        //   ,_[^)] (in ())
        //   ,_{ (in {})
        //   ,_[^]] (in [])
        //   :_.
        //   ._:
        boolean shouldPutSpace(char ch) {
            Class currentEncloser = lastEncloser();
            if (currentEncloser != DoubleQuote.class
                    &&previousNotSpaceChar_ == ','
                        && (ch != ')' && currentEncloser == Parenthesis.class
                            || ch == '{' && currentEncloser == CurlyBrace.class
                            || ch != ']' && currentEncloser == SquareBracket.class)
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

    // debugDescribeField
    static void debugDescribeClassNameHashCodeWithTag(String tag, Object object) {
        System.out.print(tag);
        debugDescribeClassNameHashCode(object);
    }

    static void debugDescribeClassNameHashCode(Object object) {
        System.out.println(convertToDebugDescriptionClassNameHashCode(object));
    }

    public static void debugDescribeField(Object object) {
        debugDescribeField(System.out, object);
    }

    public static void debugDescribeField(PrintStream printStream, Object object) {
        debugDescribeField(printStream, object, 2);
    }

    public static void debugDescribeField(PrintStream printStream, Object object, int indentSize) {
        String rawDescription;

        if (object != null && Util.isClassType(object))
            rawDescription = debugDescriptionField((Class)object);
        else
            rawDescription = debugDescriptionField(object);

        debugDescribeField(printStream, rawDescription, indentSize);
    }

    // debugDescribeMethod()
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
        else if (Util.isClassType(object))
            rawDescription = debugDescriptionMethods((Class)object);
        else
            rawDescription = debugDescriptionMethods(object);

        debugDescribeField(printStream, rawDescription, indentSize);
    }

    // debugDescribeField()
    public static void debugDescribeField(PrintStream printStream, String rawDescription, int indentSize) {
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

    // debugDescriptionField()
    private static String debugDescriptionField(Object object) {
        if (object == null)
            return "null";

        boolean isClassTypeType = Util.isClassType(object);

        if (!isClassTypeType) {
            String debugDescriptionAsSpecialType = convertToDebugDescriptionAsSpecialType(object);
            if (debugDescriptionAsSpecialType != null)
                return debugDescriptionAsSpecialType;
        }

        Class classObject = isClassTypeType ? (Class)object : object.getClass();

        StringBuilder description = new StringBuilder();
        description.append("{");
        description.append("\"value\"" + ":" + "\"" + convertToDebugDescriptionClassNameHashCode(object) + "\"" + ",");
        description.append("\"fields\"" + ":");
        description.append(debugDescriptionFields(object));
        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionFields(Object object) {
        Class classObject = Util.isClassType(object) ? (Class)object : object.getClass();

        StringBuilder description = new StringBuilder();
        description.append("{");
        while (classObject != null) {
            description.append(debugDescriptionFieldsAsClassType(object, classObject));
            classObject = (Class)classObject.getSuperclass();
        }
        description.append("}");

        return description.toString();
    }

    private static String debugDescriptionFieldsAsClassType(Object object, Class classObject) {
        Field[] fieldArray = classObject.getDeclaredFields();

        StringBuilder description = new StringBuilder();
        for (Field field : fieldArray) {
            if (!willUse(object, field))
                continue;

            description.append("{");
            description.append("\"type\"" + ":" + "\"" + field.getGenericType().getTypeName() + "\",");
            description.append("\"name\"" + ":" + "\"" + field.getName() + "\",");
            description.append("\"value\"" + ":");
            try {
                Object fieldObject = field.get(object);
                String debugDescriptionAsSpecialType = convertToDebugDescriptionAsSpecialType(fieldObject);
                if (debugDescriptionAsSpecialType != null)
                    description.append(debugDescriptionAsSpecialType);
                else
                    description.append("\"" + convertToDebugDescriptionClassNameHashCode(fieldObject) + "\"");
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

    private static boolean willUse(Object object, Field field) {
        return Util.isClassType(object) && Util.fieldHasModifier(field, Modifier.STATIC)
                || !Util.isClassType(object) && !Util.fieldHasModifier(field, Modifier.STATIC);
    }

    // convertToDebugDescriptionAsSpecialType()
    // special type: String, array, primitive type
    public static String convertToDebugDescriptionAsSpecialType(Object object) {
        if (object == null)
            return null;

        Class classObject = object.getClass();

        if (classObject == String.class) {
            return convertToDebugDescriptionAsString((String)object);
        } else if (classObject.isArray()) {
            return convertToDebugDescriptionAsArray(object);
        } else
            return convertToDebugDescriptionAsPrimitiveType(object);
    }

    static String convertToDebugDescriptionAsString(String string) {
        if (string != null)
            return "(String)\"" + string + "\"";
        else
            return null;
    }

    // debugDescriptionAsArray()
    public static String convertToDebugDescriptionAsArray(Object array) {
        if (array == null)
            return null;

        Class classObject = array.getClass();

        if (!classObject.isArray())
            return null;

        if (classObject == boolean[].class)
            return convertToDebugDescriptionAsArrayOfBoolean((boolean[])array);
        else if (classObject == char[].class)
            return convertToDebugDescriptionAsArrayOfChar((char[])array);
        else if (classObject == byte[].class)
            return convertToDebugDescriptionAsArrayOfByte((byte[])array);
        else if (classObject == short[].class)
            return convertToDebugDescriptionAsArrayOfShort((short[])array);
        else if (classObject == int[].class)
            return convertToDebugDescriptionAsArrayOfInt((int[])array);
        else if (classObject == long[].class)
            return convertToDebugDescriptionAsArrayOfLong((long[])array);
        else if (classObject == float[].class)
            return convertToDebugDescriptionAsArrayOfFloat((float[])array);
        else if (classObject == double[].class)
            return convertToDebugDescriptionAsArrayOfDouble((double[])array);
        else
            return convertToDebugDescriptionAsArrayOfObject((Object[])array);
    }

    public static String convertToDebugDescriptionAsArrayOfBoolean(boolean[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (boolean element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfChar(char[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (char element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfByte(byte[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (byte element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfShort(short[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (short element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfInt(int[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (int element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfLong(long[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (long element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfFloat(float[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (float element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfDouble(double[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (double element : array) {
            description.append(element);
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    public static String convertToDebugDescriptionAsArrayOfObject(Object[] array) {
        if (array == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (Object element : array) {
            description.append((element != null) ? convertToDebugDescriptionClassNameHashCode(element) : "null");
            description.append(",");
        }
        description.append("]");

        return description.toString();
    }

    // convertToDebugDescriptionAsPrimitiveType()
    // assume input was primitive type and converted to java.lang.* type
    public static String convertToDebugDescriptionAsPrimitiveType(Object object) {
        Class classObject = object.getClass();

        if (classObject == Boolean.class)
            return debugDescriptionAsBoolean((boolean)object);
        else if (classObject == Character.class)
            return debugDescriptionAsChar((char)object);
        else if (object instanceof Number)
            return convertToDebugDescriptionAsNumber((Number)object);
        else
            return null;
    }

    public static String debugDescriptionAsBoolean(boolean value) {
        return "(boolean)" + value;
    }

    public static String debugDescriptionAsChar(char value) {
        return "(char)" + value;
    }

    public static String convertToDebugDescriptionAsNumber(Number number) {
        Class classObject = number.getClass();

        if (classObject == Byte.class)
            return debugDescriptionAsByte((byte)number);
        else if (classObject == Short.class)
            return debugDescriptionAsShort((short)number);
        else if (classObject == Integer.class)
            return debugDescriptionAsInt((int)number);
        else if (classObject == Long.class)
            return debugDescriptionAsLong((long)number);
        else if (classObject == Float.class)
            return debugDescriptionAsFloat((float)number);
        else if (classObject == Double.class)
            return debugDescriptionAsDouble((double)number);
        else
            return null;
    }

    public static String debugDescriptionAsByte(byte value) {
        return "(byte)" + value;
    }

    public static String debugDescriptionAsShort(short value) {
        return "(short)" + value;
    }

    public static String debugDescriptionAsInt(int value) {
        return "(int)" + value;
    }

    public static String debugDescriptionAsLong(long value) {
        return "(long)" + value;
    }

    public static String debugDescriptionAsFloat(float value) {
        return "(float)" + value;
    }

    public static String debugDescriptionAsDouble(double value) {
        return "(double)" + value;
    }

    // convertToDebugDescriptionClassNameHashCode()
    public static String convertToDebugDescriptionClassNameHashCode(Object object) {
        if (object != null)
            return String.format("%s@%08x", object.getClass().getName(), object.hashCode());
        else
            return null;
    }

    // debugDescriptionMethods()
    private static String debugDescriptionMethods(Object object) {
        final Class classObject = Util.isClassType(object) ? (Class)object : object.getClass();

        // TODO copy debugDescriptionFields(Object object)
        StringBuilder description = new StringBuilder();
        description.append("{");
        description.append("\"type\"" + ":" + "\"" + classObject.getName() + "\"" + ",");
        description.append("\"value\"" + ":" + "\"" + object + "\"" + ",");
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
            if (Util.isClassType(object) && !Util.methodHasModifier(method, Modifier.STATIC)
                    || !Util.isClassType(object) && Util.methodHasModifier(method, Modifier.STATIC))
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
        playground001();
    }

    private static void playground000() {
        Debug debug = new Debug();

//          System.out.println(debugDescriptionFields(debug));
        debugDescribeField(debug);
        debugDescribeField((Object)debug);
//          debugDescribeField(System.out, null);
//          debugDescribeField(System.out, Debug.class);
//          debugDescribeField(System.out, new Object());
//  
//  //          Debug[] debugArray = { new Debug(), new Debug() };
//  //          debugDescribeField(System.out, debugArray);
//  
        int[] intArray = { 1, 2, 3 };
        debugDescribeField(System.out, intArray);
//          haven.UIPanel.Dispatcher dispatcher = new haven.UIPanel.Dispatcher();
//          dispatcher
//          debugDescribeMethod(Debug.class);
        debugDescribeField(null);
        debugDescribeField(new java.lang.Integer(1));
    }

    // debugDescriptionPrimitiveType() test
    private static void playground001() {
        boolean bo = true;
        byte by = 2;
        char ch = 3;
        short sh = 4;
        int in = 5;
        long lo = 6;
        float fl = 7.8f;
        double dou = 9.0d;

        System.out.println(convertToDebugDescriptionAsPrimitiveType(bo));
        System.out.println(convertToDebugDescriptionAsPrimitiveType(by));
        System.out.println(convertToDebugDescriptionAsPrimitiveType(ch));
        System.out.println(convertToDebugDescriptionAsPrimitiveType(sh));
        System.out.println(convertToDebugDescriptionAsPrimitiveType(in));
        System.out.println(convertToDebugDescriptionAsPrimitiveType(fl));
        System.out.println(convertToDebugDescriptionAsPrimitiveType(dou));

        System.out.println();

        boolean[] boA = { true, false };
        byte[] byA = { 2, 4 };
        char[] chA = { 3, 6 };
        short[] shA = { 4, 8 };
        int[] inA = { 5, 10 };
        long[] loA = { 6, 12 };
        float[] flA = { 7.8f, 15.6f };
        double[] douA = { 9.0d, 18.0d };

        System.out.println(convertToDebugDescriptionAsArray(boA));
        System.out.println(convertToDebugDescriptionAsArray(byA));
        System.out.println(convertToDebugDescriptionAsArray(chA));
        System.out.println(convertToDebugDescriptionAsArray(shA));
        System.out.println(convertToDebugDescriptionAsArray(inA));
        System.out.println(convertToDebugDescriptionAsArray(flA));
        System.out.println(convertToDebugDescriptionAsArray(douA));

        System.out.println();

        System.out.println(convertToDebugDescriptionAsArray(null));
        System.out.println(convertToDebugDescriptionAsArray(bo));
        System.out.println(convertToDebugDescriptionAsArrayOfBoolean(null));
    }
}
