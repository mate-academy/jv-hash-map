package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INCREASE_FACTOR = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int capacity;
    private float loadFactor;
    private Node<K, V>[] table;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int hash = hash(key);
        int index = getNodeIndexInTable(hash, capacity);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash
                    && (currentNode.key == key
                    || currentNode.key != null
                    && currentNode.key.equals(key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int hash = hash(key);
        int index = getNodeIndexInTable(hash, capacity);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.hash == hash
                    && (currentNode.key == key
                    || currentNode.key != null
                    && currentNode.key.equals(key))) {
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
    /* ------- private methods ------- */

    private void putForNullKey(V value) {
        Node<K, V> nullKeyNode = table[0];
        while (nullKeyNode != null) {
            if (nullKeyNode.key == null) {
                nullKeyNode.value = value;
                return;
            }
            nullKeyNode = nullKeyNode.next;
        }
        addNode(0, null, value, 0);
    }

    private V getForNullKey() {
        Node<K, V> nullKeyNode = table[0];
        while (nullKeyNode != null) {
            if (nullKeyNode.key == null) {
                return nullKeyNode.value;
            }
            nullKeyNode = nullKeyNode.next;
        }
        return null;
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
        float threshold = capacity * loadFactor;
        if (size > threshold) {
            resizeTable();
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getNodeIndexInTable(int hash, int capacity) {
        return Math.abs(hash % capacity);
    }

    private void resizeTable() {
        int newCapacity = capacity * INCREASE_FACTOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                int newIndex = getNodeIndexInTable(oldNode.hash, newCapacity);
                Node<K, V> next = oldNode.next;
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
            table = newTable;
            capacity = newCapacity;
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
