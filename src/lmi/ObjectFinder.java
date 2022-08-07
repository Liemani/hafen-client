package lmi;

import lmi.collection.Array;

class ObjectFinder {
    // static fields
    private static Array<Object> path_ = null;

    // Initializing the Finder
    static void init() {
        path_ = new Array<Object>();
    }

    static boolean isEmpty() {
        return path_.isEmpty();
    }

    static int count() {
        return path_.count();
    }

    // Accessing Elements
    static Object last() {
        return path_.last();
    }

    // Adding Elements
    static void append(Object object) {
        path_.append(object);
    }

    // Removing Elements
    static Object removeLast() {
        return path_.removeLast();
    }

    // Controlling Path
    // if classObjectToReset is not null, remove all elements from path
    // if classObjectToReset is null, find from last()
    // TODO remove dependencey of input from Util.getObjectByInputFromField() and Util.getObjectByInputFromMethodo()
    static Object find(Util.MemberType type, Class classObjectToReset) throws Exception {
        if (classObjectToReset != null)
            path_.removeAll();

        boolean typeIsField = type.isField();

        if (typeIsField && classObjectToReset == null && last() == null
                || !typeIsField && last() == null) {
            System.out.println("cannot progress more!");
            throw new Exception();
        }

        if (classObjectToReset == null)
            classObjectToReset = last().getClass();

        Object object = null;
        if (typeIsField)
            object = Util.getObjectByInputFromField(last(), classObjectToReset);
        else
            object = Util.getObjectByInputFromMethod(last(), classObjectToReset);

        return object;
    }
}
