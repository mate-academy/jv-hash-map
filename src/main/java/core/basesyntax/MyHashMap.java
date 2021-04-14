package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY_INCREASING = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = table[hash(key)];
        if (node == null) {
            table[hash(key)] = new Node<>(key, value);
            size++;
            return;
        }
        while (node.next != null || key == node.key || key != null && key.equals(node.key)) {
            if (key == node.key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> returnedValue = table[hash(key)];
        while (returnedValue != null) {
            if (key == returnedValue.key || key != null && key.equals(returnedValue.key)) {
                return returnedValue.value;
            }
            returnedValue = returnedValue.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        if (size == threshold) {
            capacity = capacity * DEFAULT_CAPACITY_INCREASING;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            transfer();
        }
    }

    private void transfer() {
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
