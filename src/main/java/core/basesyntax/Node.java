package core.basesyntax;

public class Node<K, V> {
    protected final K key;
    protected V value;
    protected Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
