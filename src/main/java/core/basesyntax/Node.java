package core.basesyntax;

public class Node<K, V> {
    protected final int hash;
    protected final K key;
    protected V value;
    protected Node<K, V> next;

    public Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
}
