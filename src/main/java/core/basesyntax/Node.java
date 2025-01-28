package core.basesyntax;

public class Node<K, V> {
    private final K key;
    private final int hash;
    private V value;
    private Node<K,V> next;

    public Node(K key, int hash, V value, Node<K,V> next) {
        this.key = key;
        this.hash = hash;
        this.value = value;
        this.next = next;
    }
}
