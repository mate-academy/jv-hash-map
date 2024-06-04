package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
        this.threshold = (int) (capacity * loadFactor);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode()) ^ (key.hashCode() >>> 16);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = (capacity - 1) & hash;

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                node.value = value;
                return;
            }
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = (capacity - 1) & hash;

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = (newCapacity - 1) & node.hash;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
