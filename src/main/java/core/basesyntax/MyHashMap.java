package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TWO_FOR_DOUBLING = 2;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive");
        }
        this.loadFactor = loadFactor;
        threshold = (int) (initialCapacity * loadFactor);
        table = new Node[initialCapacity];
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == null || node.key.equals(key)) {
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

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
        } else {
            putNonNullKey(key, value);
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private void putForNullKey(V value) {
        if (table[0] != null) {
            Node<K, V> element = table[0];
            if (element.key != null && element.next == null) {
                element.next = new Node<>(0, null, value, null);
                size++;
            } else {
                while (element != null) {
                    if (element.key == null) {
                        element.value = value;
                    }
                    element = element.next;
                }
            }
        } else {
            table[0] = newNode(0, null, value, null);
            size++;
        }
    }

    private void putNonNullKey(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> element = table[index]; element != null; element = element.next) {
            Object k = element.key;
            if (element.hash == hash && (k == key || key.equals(k))) {
                element.value = value;
                return;
            }
        }
        createEntry(hash, key, value, index);
    }

    private void createEntry(int hash, K key, V value, int bucketIndex) {
        Node<K, V> element = table[bucketIndex];
        table[bucketIndex] = newNode(hash, key, value, element);
        size++;
    }

    private int indexFor(int h, int length) {
        return Math.abs(h) % length;
    }

    private void resize() {
        int newCapacity = table.length * TWO_FOR_DOUBLING;
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = indexFor(node.hash, newTable.length);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
    }

    private Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private MyHashMap.Node<K, V> next;

        Node(int hash, K key, V value, MyHashMap.Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
