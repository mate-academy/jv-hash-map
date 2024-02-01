package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[16];
    }

    @Override
    public void put(K key, V value) {
        growIfNeeded();
        putNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = calculatorIndexOfBucket(key);
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int calculatorIndexOfBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void putNode(K key, V value) {
        int bucketIndex = calculatorIndexOfBucket(key);
        Node<K, V> node = table[bucketIndex];
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
        table[bucketIndex] = new Node<>(key, value, null);
        size++;
    }

    private void growIfNeeded() {
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (size > threshold) {
            copyArrayInNewBiggerArray();
        }
    }

    private void copyArrayInNewBiggerArray() {
        int newCapacity = table.length * 2;
        Node<K, V>[] copyTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : copyTable) {
            while (node != null) {
                putNode(node.key, node.value);
                node = node.next;
            }
        }
    }
}
