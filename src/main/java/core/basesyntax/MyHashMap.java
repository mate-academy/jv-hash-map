package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException();
        }
        table = new Node[initialCapacity];
    }

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        Node<K, V> node = table[getHash(key)];
        while (node != null) {
            if (isEqual(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[getHash(key)] = new Node<>(key, value, table[getHash(key)]);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getHash(key)];
        while (node != null) {
            if (isEqual(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        if (size >= table.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] tempTable = table;
            table = new Node[table.length * MULTIPLIER];
            for (Node<K, V> node : tempTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private boolean isEqual(K key1, K key2) {
        return key1 == key2
            || key1 != null && key1.equals(key2);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
