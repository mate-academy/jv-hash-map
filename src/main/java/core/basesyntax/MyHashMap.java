package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    private MyHashMap(int initialCapacity) {
        this.table = new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            node.value = value;
        } else {
            int hash = hash(key);
            int index = indexFor(hash, table.length);
            addNode(hash, key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();

    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
        } else {
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash || node.key.equals(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K,V>[] newTable = new Node[newCapacity];
        for (Node<K, V> bucket : table) {
            Node<K, V> node = bucket;
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = indexFor(node.hash, newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
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
