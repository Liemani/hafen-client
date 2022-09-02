// https://developer.apple.com/documentation/swift/dictionary/
// this class mimic Swift::Dictionary
package lmi.collection;

public class Dictionary<Key, Value> extends java.util.TreeMap<Key, Value> {
    // Accessing Keys and Values
    public Value subscript(Key key) { return super.get(key); }

    // Adding Keys and Values
    public Value updateValueForKey(Value value, Key key) { return super.put(key, value); }

    // Removing Keys and Values
    public Value removeValueForKey(Key key) { return super.remove(key); };
}
