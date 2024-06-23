package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private int threshold;

    private V nullKeyValue = null;
    private boolean nullKeyPresent = false;

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

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (!nullKeyPresent) {
                size++;
            }
            nullKeyValue = value;
            nullKeyPresent = true;
            return;
        }

        int hash = hash(key);
        int index = indexFor(hash, capacity);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                node.value = value; // Replace existing value
                return;
            }
        }

        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyPresent ? nullKeyValue : null;
        }

        int hash = hash(key);
        int index = indexFor(hash, capacity);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = indexFor(hash(node.key), newCapacity);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
        threshold = (int) (capacity * loadFactor);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private int hash(K key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }
}
