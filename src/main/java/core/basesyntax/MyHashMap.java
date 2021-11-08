package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY = 16;
    private static final int CAPACITY_GROWTH_MULTIPLIER = 2;
    private int threshold;
    private int size;
    private Node<K, V> [] table;

    public MyHashMap() {
        table = new Node[CAPACITY];
        threshold = (int) (CAPACITY * LOAD_FACTOR);
    }
    @Override
    public void put(K key, V value) {
        checkCapacity();
        int index = computeIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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
            table[index] = new Node<>(key, value, null);
            size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[computeIndex(key)];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
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

    private void checkCapacity() {
        if (size == threshold) {
            changeCapacity();
        }
    }

    private void changeCapacity() {
        Node<K, V>[] nodes = new Node[size];
        int index = 0;
        for (Node<K, V> node : table) {
            while (node != null) {
                nodes[index++] = node;
                node = node.next;
            }
        }
        table = new Node[table.length * CAPACITY_GROWTH_MULTIPLIER];
        size = 0;
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> node : nodes) {
            put(node.key, node.value);
        }
    }

    private int computeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<T, V> {
        private final T key;
        private V value;
        private Node<T,V> next;

        public Node(T key, V value, Node<T, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
