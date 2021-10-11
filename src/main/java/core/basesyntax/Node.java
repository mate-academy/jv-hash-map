package core.basesyntax;

public class Node<K, V> {
    public int hash;
    public K key;
    public V value;
    Node<K, V> next;
}
