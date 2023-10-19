package core.basesyntax;

public class Node<K, V> {
    private final K key;
    private V value;
    private final int hash;
    private Node<K, V> next;

    public Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public int getHash() {
        return hash;
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

    public Node<K, V> getNext() {
        return next;
    }

    public void setNext(Node<K, V> next) {
        this.next = next;
    }

    public boolean keyEquals(K other) {
        if (key == null) {
            return other == null;
        }
        return key.equals(other);
    }
}
