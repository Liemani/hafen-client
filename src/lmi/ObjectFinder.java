package lmi;

public class ObjectFinder {
    // static fields
    private static Array<Object> _path = null;

    // Initializing the Finder
    public static void init() {
        _path = new Array<Object>();
    }

    public static boolean isEmpty() {
        return _path.isEmpty();
    }

    public static int count() {
        return _path.count();
    }

    // Accessing Elements
    public static Object last() {
        return _path.last();
    }

    // Adding Elements
    public static void moveForward(Object object) {
        if (object == null)
            return;

        _path.append(object);
    }

    // Removing Elements
    public static Object moveBackward() {
        if (isEmpty())
            return null;

        return _path.removeLast();
    }

    // Controlling Path
    // if classObjectToReset is not null, remove all elements from path
    // if classObjectToReset is null, find from last()
    // TODO remove dependencey of input from Util.getObjectByInputFromField() and Util.getObjectByInputFromMethodo()
    public static Object find(Util.MemberType type, Class classObjectToReset) throws Exception {
        if (classObjectToReset != null)
            _path.removeAll();

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
    public static void listLastAsWidget() {
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
