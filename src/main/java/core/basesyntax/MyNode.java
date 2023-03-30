package core.basesyntax;

public class MyNode<K, V> {
    private MyNode<K, V> next;
    private final K key;
    private V value;

    public MyNode(MyNode<K, V> next, K key, V value) {
        this.next = next;
        this.key = key;
        this.value = value;
    }

    public MyNode<K, V> getNext() {
        return next;
    }

    public void setNext(MyNode<K, V> next) {
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

