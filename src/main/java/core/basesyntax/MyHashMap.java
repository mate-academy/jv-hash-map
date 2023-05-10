package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private float loadFactor;
    private int size;
    private Node<K, V>[] table;
    private Node<K, V> nullKeyNode;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (nullKeyNode == null) {
                nullKeyNode = new Node<K, V>(key, value, null);
                size++;
            } else {
                nullKeyNode.value = value;
            }
            return;
        }
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(index, key, value);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            if (nullKeyNode == null) {
                return null;
            }
            return nullKeyNode.value;
        }
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key.equals(key)) {
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

    private void addNode(int index, K key, V value) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(key, value, node);
        size++;
        if ((float) size / capacity >= loadFactor) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                int index = hash(node.key, newCapacity);
                Node<K, V> next = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
        capacity = newCapacity;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return hash(key, capacity);
    }

    private int hash(K key, int capacity) {
        return Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
