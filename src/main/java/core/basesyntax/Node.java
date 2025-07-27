package core.basesyntax;

public class Node<K, V> {

    private int hash;
    private K key;
    private V value;
    private Node<K, V> next;
}
