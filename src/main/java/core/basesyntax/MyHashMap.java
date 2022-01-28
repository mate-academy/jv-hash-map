package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node [table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putInEmptyBucket(K key, V value, int index) {
        table[index] = new Node<>(key, value,null);
        size++;
    }

    private void putInNoEmptyBucket(K key, V value, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            putInEmptyBucket(key, value, index);
        } else {
            putInNoEmptyBucket(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
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

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
