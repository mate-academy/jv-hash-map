package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final double loadFactor;
    private int threshold; // capacity to resize

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (capacity * loadFactor);
    }

    public MyHashMap(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
    }

    @Override
    public void put(K key, V value) {
        if (table[hash(key)] != null) { // Collision
            Node<K, V> node = table[hash(key)];
            while (node != null) {
                if (key == node.key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, hash(key), null);
                    break;
                }
                node = node.next;
            }

        } else {
            Node<K, V> node = new Node<>(key, value, hash(key), null);
            table[hash(key)] = node;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    private void resize() {
        size = 0;
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> first : oldTable) {
            Node<K, V> node = first;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }
}
