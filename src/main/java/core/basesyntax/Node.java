package core.basesyntax;

public class Node<K, V> {
    private final int hash;
    private final K key;
    private V value;
    private Node<K, V> next;

    public Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public int getHash() {
        return hash;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return String.valueOf(key) + "=" + String.valueOf(value);
    }
}
