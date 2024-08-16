package core.basesyntax;

public class Node<K,V> {
    private final int hash;
    private final K key;
    private V value;
    private Node<K, V> next;

    Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey() {
        return key;
    }

    public final V getValue() {
        return value;
    }

    public int getHash() {
        return hash;
    }

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public final void setValue(V newValue) {
        value = newValue;
    }
}
