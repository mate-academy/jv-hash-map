package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucket = countBucket(key);
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = table[bucket];
        while (newNode != null) {
            if (key == newNode.key || (key != null && key.equals(newNode.key))) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            newNode = newNode.next;
        }
        table[bucket] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[countBucket(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        int newSize = oldTable.length * 2;
        table = new Node[newSize];
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (newSize * LOAD_FACTOR);
    }

    private int countBucket(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
