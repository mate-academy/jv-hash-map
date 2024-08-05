package core.basesyntax;

import java.util.Objects;

public class Node<K,V> {
    private K key;
    private V value;
    Node next;
    Node head;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Node(K key, V value, Node next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public Node<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    public Node<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Node<?, ?> entry = (Node<?, ?>) object;
        return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
