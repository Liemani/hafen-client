// https://developer.apple.com/documentation/swift/array/
// this class follow Swift::Array
package lmi.collection;

public class Array<Element> extends java.util.ArrayList<Element> {
    // constructor
    public Array() {
        super();
    }

    public Array(int capacity) {
        super(capacity);
    }

    // Inspecting and Array
    public boolean isEmpty() {
        return super.isEmpty();
    }

    public int count() {
        return super.size();
    }

    // Accessing Elements
    public Element subscript(int index) {
        try {
            return super.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Element first() {
        try {
            return !isEmpty() ? super.get(0) : null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Element last() {
        try {
            return !isEmpty() ? super.get(count() - 1) : null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    // Adding Elements
    public void append(Element element) {
        super.add(element);
    }

    // Removing Elements
    // assume: the collection is not empty
    public Element removeFirst() {
        return remove(0);
    }

    // Finding Elements
    public boolean containsWhere(java.util.function.Predicate<Element> predicate) {
        for (Element element : this)
            if (predicate.test(element))
                return true;
        return false;
    }

    // assume: the collection is not empty
    public Element removeLast() {
        return remove(count() - 1);
    }

    public void removeAll() {
        super.clear();
    }
}
