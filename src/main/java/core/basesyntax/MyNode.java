package core.basesyntax;

public class MyNode<K, V> {
    private final int hashKey;
    private final K key;
    private V value;
    private MyNode<K, V> next;

    MyNode(K key, V value) {
        this.key = key;
        this.hashKey = (key != null) ? key.hashCode() : 0;
        this.value = value;
        this.next = null;
    }

    public int getHashKey() {
        return hashKey;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public MyNode<K, V> getNext() {
        return next;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNext(MyNode<K, V> next) {
        this.next = next;
    }
}
