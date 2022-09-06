// https://developer.apple.com/documentation/swift/array/
// this class mimic Swift::Array
package lmi;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Array<E> extends ArrayList<E> {
    // Constructor
    public Array() { super(); }
    public Array(int capacity) { super(capacity); }

    // Inspecting and Array
    public boolean isEmpty() { return super.isEmpty(); }
    public int count() { return super.size(); }

    // Accessing Elements
    public E subscript(int index) {
        try {
            return super.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public E first() {
        try {
            return !isEmpty() ? super.get(0) : null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public E last() {
        try {
            return !isEmpty() ? super.get(count() - 1) : null;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    // Adding Elements
    public void append(E element) { super.add(element); }

    // Removing Elements
    // assume: the collection is not empty
    public E removeFirst() { return remove(0); }
    public void removeAllWhere(Predicate<E> predicate) { super.removeIf(predicate); }

    // Finding Elements
    public boolean containsWhere(Predicate<E> predicate) {
        for (E element : this)
            if (predicate.test(element))
                return true;
        return false;
    }

    // assume: the collection is not empty
    public E removeLast() { return remove(count() - 1); }
    public void removeAll() { super.clear(); }
}
