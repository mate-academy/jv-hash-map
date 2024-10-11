package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_ARRAY_CAPACITY = 16;
    private static final int RESIZE_NUMBER = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_ARRAY_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > (table.length * LOAD_FACTOR)) {
            int capacity = DEFAULT_ARRAY_CAPACITY * RESIZE_NUMBER;
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];

        }
        size++;
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] currentTable = table;
        table = new Node[currentTable.length * RESIZE_NUMBER];
    }

    private static class Node<K, V> {
        final K key;
        final V value;
        final Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
