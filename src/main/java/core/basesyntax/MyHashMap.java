package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V> []table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> node = table[hash];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
        table[hash] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> node = table[hash];
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public void resize() {
        capacity *= RESIZE_FACTOR;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> nodes : oldTable) {
            while (nodes != null) {
                put(nodes.key, nodes.value);
                nodes = nodes.next;
            }
        }
    }
}
