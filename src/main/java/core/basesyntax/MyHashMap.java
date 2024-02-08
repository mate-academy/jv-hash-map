package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] table;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (node.key == null && key == null || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(key, value, bucketIndex);
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (node.key == null && key == null || node.key != null && node.key.equals(key)) {
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

    private void addNode(K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
        if (size >= table.length * loadFactor) {
            resize();
        }
    }

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                int newIndex = getBucketIndex(oldNode.key);
                Node<K, V> next = oldNode.next;
                oldNode.next = table[newIndex];
                table[newIndex] = oldNode;
                size++;
                oldNode = next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
