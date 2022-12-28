package core.basesyntax;

public class Node<K, V> {
    private final int hash;
    private final K key;
    private V value;
    private Node<K, V> next;

    Node(K key, V value, int hash) {
        this.hash = hash;
        this.key = key;
        this.value = value;
    }
}


