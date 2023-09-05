package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_COEFFICIENT = 2;
    private int treshold;
    private Node<K, V>[] table;
    private int capacity;
    private int size = 0;

    public MyHashMap() {
        treshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == treshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        capacity = capacity * GROW_COEFFICIENT;
        treshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] copyTable = table;
        Node<K, V>[] newTable = new Node[capacity];
        table = newTable;
        size = 0;
        for (int i = 0; i < copyTable.length; i++) {
            Node<K, V> node = copyTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
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
