package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private float loadFactor;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be non-negative");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive");
        }
        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * loadFactor) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }

        Node<K, V> current = table[index];
        Node<K, V> prev = null;
        while (current != null) {
            if (isKeysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (isKeysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private boolean isKeysEqual(K key1, K key2) {
        if (key1 == null) {
            return key2 == null;
        }
        return key1.equals(key2);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public int getSize() {
        return size;
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
