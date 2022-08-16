// Integer type of object's field would be described as like primitive type
package lmi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.lang.Number;

import java.io.PrintStream;

import java.util.Iterator;
import java.util.AbstractCollection;
import java.util.ArrayList;

import lmi.Util;

public class Debug {
    static boolean isPrint_;

    static void init() {
        isPrint_ = false;
    }

    static void toggleIsPrint() {
        isPrint_ = !isPrint_;
        System.out.println("[isPrint: " + isPrint_ + "]");
    }

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

    // printStackTrace
    public static void printStackTraceWithTag(String tag) {
        if (!isPrint_)
            return;

        System.out.print(tag);
        new Exception().printStackTrace();
    }

    // describeField
    public static void describeClassNameHashCodeWithTag(String tag, Object object) {
        System.out.print(tag);
        describeClassNameHashCode(object);
    }

    public static void describeClassNameHashCode(Object object) {
        System.out.println(convertToDebugDescriptionClassNameHashCode(object));
    }

    public static void describeField(Object object) {
        describeField(System.out, object);
    }

    public static void describeField(PrintStream printStream, Object object) {
        describeField(printStream, object, 2);
    }

    public static void describeField(PrintStream printStream, Object object, int indentSize) {
        if (!isPrint_)
            return;

        String rawDescription;

        if (object != null && Util.isClassType(object))
            rawDescription = descriptionField((Class)object);
        else
            rawDescription = descriptionField(object);

        describeField(printStream, rawDescription, indentSize);
    }

    // describeMethod()
    public static void describeMethod(Object object) {
        describeMethod(System.out, object);
    }

    public static void describeMethod(PrintStream printStream, Object object) {
        describeMethod(printStream, object, 2);
    }

    public static void describeMethod(PrintStream printStream, Object object, final int indentSize) {
        String rawDescription;

        if (object == null) {
            rawDescription = "null";
        }
        else if (Util.isClassType(object))
            rawDescription = descriptionMethods((Class)object);
        else
            rawDescription = descriptionMethods(object);

        describeField(printStream, rawDescription, indentSize);
    }

    // describeField()
    public static void describeField(PrintStream printStream, String rawDescription, int indentSize) {
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

    // descriptionField()
    private static String descriptionField(Object object) {
        if (object == null)
            return "null";

        boolean isClassTypeType = Util.isClassType(object);

        if (!isClassTypeType) {
            String descriptionAsSpecialType = convertToDebugDescriptionAsSpecialType(object);
            if (descriptionAsSpecialType != null)
                // TODO clean this dirty but fast implementation
                return "{\"type\":" + convertToDebugDescriptionClassNameHashCode(object) + ",\"value\":" +  descriptionAsSpecialType + "}";
        }

        Class classObject = isClassTypeType ? (Class)object : object.getClass();

        StringBuilder description = new StringBuilder();
        description.append("{");
        description.append("\"value\"" + ":" + "\"" + convertToDebugDescriptionClassNameHashCode(object) + "\"" + ",");
        description.append("\"fields\"" + ":");
        description.append(descriptionFields(object));
        description.append("}");

        return description.toString();
    }

    private static String descriptionFields(Object object) {
        Class classObject = Util.isClassType(object) ? (Class)object : object.getClass();

        StringBuilder description = new StringBuilder();
        description.append("{");
        while (classObject != null) {
            description.append(descriptionFieldsAsClassType(object, classObject));
            classObject = (Class)classObject.getSuperclass();
        }
        description.append("}");

        return description.toString();
    }

    private static String descriptionFieldsAsClassType(Object object, Class classObject) {
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
                String descriptionAsSpecialType = convertToDebugDescriptionAsSpecialType(fieldObject);
                if (descriptionAsSpecialType != null)
                    description.append(descriptionAsSpecialType);
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
    // special type: String, array, AbstractCollection, primitive type
    public static String convertToDebugDescriptionAsSpecialType(Object object) {
        if (object == null)
            return null;

        Class classObject = object.getClass();

        if (classObject == String.class) {
            return convertToDebugDescriptionAsString((String)object);
        } else if (classObject.isArray()) {
            return convertToDebugDescriptionAsArray(object);
        } else if (object instanceof java.util.AbstractCollection) {
            return convertToDebugDescriptionAsCollection((AbstractCollection<?>)object);
        } else
            return convertToDebugDescriptionAsPrimitiveType(object);
    }

    static String convertToDebugDescriptionAsString(String string) {
        if (string != null)
            return "(String)\"" + string + "\"";
        else
            return null;
    }

    // descriptionAsArray()
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

    // convertToDebugDescriptionAsCollection
    static <E> String convertToDebugDescriptionAsCollection(AbstractCollection<E> collection) {
        if (collection == null)
            return null;

        StringBuilder description = new StringBuilder();

        description.append("[");
        for (E element : collection) {
            description.append(element);
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
            return descriptionAsBoolean((boolean)object);
        else if (classObject == Character.class)
            return descriptionAsChar((char)object);
        else if (object instanceof Number)
            return convertToDebugDescriptionAsNumber((Number)object);
        else
            return null;
    }

    public static String descriptionAsBoolean(boolean value) {
        return "(boolean)" + value;
    }

    public static String descriptionAsChar(char value) {
        return "(char)" + value;
    }

    public static String convertToDebugDescriptionAsNumber(Number number) {
        Class classObject = number.getClass();

        if (classObject == Byte.class)
            return descriptionAsByte((byte)number);
        else if (classObject == Short.class)
            return descriptionAsShort((short)number);
        else if (classObject == Integer.class)
            return descriptionAsInt((int)number);
        else if (classObject == Long.class)
            return descriptionAsLong((long)number);
        else if (classObject == Float.class)
            return descriptionAsFloat((float)number);
        else if (classObject == Double.class)
            return descriptionAsDouble((double)number);
        else
            return null;
    }

    public static String descriptionAsByte(byte value) {
        return "(byte)" + value;
    }

    public static String descriptionAsShort(short value) {
        return "(short)" + value;
    }

    public static String descriptionAsInt(int value) {
        return "(int)" + value;
    }

    public static String descriptionAsLong(long value) {
        return "(long)" + value;
    }

    public static String descriptionAsFloat(float value) {
        return "(float)" + value;
    }

    public static String descriptionAsDouble(double value) {
        return "(double)" + value;
    }

    // convertToDebugDescriptionClassNameHashCode()
    public static String convertToDebugDescriptionClassNameHashCode(Object object) {
        if (object != null)
            return String.format("%s@%08x", object.getClass().getName(), object.hashCode());
        else
            return null;
    }

    // descriptionMethods()
    private static String descriptionMethods(Object object) {
        final Class classObject = Util.isClassType(object) ? (Class)object : object.getClass();

        // TODO copy descriptionFields(Object object)
        StringBuilder description = new StringBuilder();
        description.append("{");
        description.append("\"type\"" + ":" + "\"" + classObject.getName() + "\"" + ",");
        description.append("\"value\"" + ":" + "\"" + object + "\"" + ",");
        description.append("\"methods\"" + ":");
        description.append(descriptionMethods(object, classObject));
        description.append("}");

        return description.toString();
    }

    private static String descriptionMethods(Object object, Class classObject) {
        StringBuilder description = new StringBuilder();

        description.append("{");

        while (classObject != null) {
            description.append(descriptionMethodsAsClass(object, classObject));
            classObject = (Class)classObject.getGenericSuperclass();
        }

        description.append("}");

        return description.toString();
    }

    private static String descriptionMethodsAsClass(Object object, Class classObject) {
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

//          System.out.println(descriptionFields(debug));
        describeField(debug);
        describeField((Object)debug);
//          describeField(System.out, null);
//          describeField(System.out, Debug.class);
//          describeField(System.out, new Object());
//  
//  //          Debug[] debugArray = { new Debug(), new Debug() };
//  //          describeField(System.out, debugArray);
//  
        int[] intArray = { 1, 2, 3 };
        describeField(System.out, intArray);
//          haven.UIPanel.Dispatcher dispatcher = new haven.UIPanel.Dispatcher();
//          dispatcher
//          describeMethod(Debug.class);
        describeField(null);
        describeField(new java.lang.Integer(1));
    }

    // descriptionPrimitiveType() test
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
