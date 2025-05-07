package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INCREASE_FACTOR = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private float loadFactor;
    private Node<K, V>[] table;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float initialLoadFactor) {
        loadFactor = initialLoadFactor;
        table = new Node[initialCapacity];
    }

    @Override
    public void put(K key, V value) {
        int index = getNodeIndexInTable(key, table.length);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null
                    && currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = getNodeIndexInTable(key, table.length);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null
                    && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        float threshold = table.length * loadFactor;
        if (size > threshold) {
            resizeTable();
        }
    }

    private int getNodeIndexInTable(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resizeTable() {
        int newCapacity = table.length * INCREASE_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                int newIndex = getNodeIndexInTable(oldNode.key, newCapacity);
                Node<K, V> next = oldNode.next;
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
            table = newTable;
        }
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
