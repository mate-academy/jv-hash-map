package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = hash(key) % table.length;
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = newNode;
            size++;
            return;
        }
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
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

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void grow() {
        size = 0;
        Node<K, V>[] oldNodes = table;
        table = new Node[DEFAULT_CAPACITY * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> oldNode : oldNodes) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }
}
