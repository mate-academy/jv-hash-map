package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_COEFFICIENT = 2;
    private int treshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int capacity = DEFAULT_CAPACITY;
    private int size = 0;

    @Override
    public void put(K key, V value) {
        int index = Math.abs(hash(key));
        Node<K, V> node = table[index];
        while (node != null) {
            if (((node.key == null || key == null) && node.key == key)
                    || ((node.key != null && key != null) && node.key.equals(key))) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(index, key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size == treshold) {
            resize();
        }
    }

    public void resize() {
        int oldCapacity = capacity;
        capacity = capacity * GROW_COEFFICIENT;
        treshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[capacity];
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> nodeNext = node.next;
                int index = Math.abs(hash(node.key));
                node.next = newTable[index];
                newTable[index] = node;
                node = nodeNext;
            }
        }
        table = newTable;
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key));
        Node<K, V> node = table[index];
        while (node != null) {
            if (((node.key == null || key == null) && node.key == key)
                    || ((node.key != null && key != null) && node.key.equals(key))) {
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

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() % capacity;
    }

    static class Node<K, V> {
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
