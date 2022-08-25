package lmi;

import lmi.collection.*;

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
    static void moveForward(Object object) {
        if (object == null)
            return;

        path_.append(object);
    }

    // Removing Elements
    static Object moveBackward() {
        if (isEmpty())
            return null;

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

        if (typeIsField && classObjectToReset == null && ObjectFinder.last() == null
                || !typeIsField && ObjectFinder.last() == null) {
            System.out.println("cannot progress more!");
            throw new Exception();
        }

        if (classObjectToReset == null)
            classObjectToReset = ObjectFinder.last().getClass();

        Object object = null;
        if (typeIsField)
            object = Util.getObjectByInputFromField(ObjectFinder.last(), classObjectToReset);
        else
            object = Util.getObjectByInputFromMethod(ObjectFinder.last(), classObjectToReset);

        return object;
    }

    // listLastAsWidget()
    static void listLastAsWidget() {
        Object currentObject = last();
        if (currentObject instanceof haven.Widget)
            iterateWidget((haven.Widget)currentObject, 0);
        else
            System.out.println("currnet object is not instance of haven.Widget");
    }

    private static void iterateWidget(haven.Widget widget, int indentCount) {
        for (; widget != null; widget = widget.next) {
            Util.insertIndent(indentCount);
            System.out.println(widget.getClass().getName());
            iterateWidget(widget.child, indentCount + 1);
        }
    }
}
