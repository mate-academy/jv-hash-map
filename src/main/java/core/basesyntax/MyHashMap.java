package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeCapacity();
        Node<K, V> node = new Node<>(key, value);
        int index = indexOf(hash(key));
        if (table[index] == null) {
            createBucket(index, node);
        } else {
            updateBucket(index, node);
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexOf(hash(key));
        Node<K, V> bucket = table[index];
        while (bucket != null) {
            if (objectsAreEqual(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexOf(int hash) {
        return Math.abs(hash % table.length);
    }

    private void resizeCapacity() {
        if (size < table.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * CAPACITY_MULTIPLIER];
        int oldSize = size;
        size = 0;
        transferDataToNewEmptyTable(oldTable, oldSize);
    }

    private boolean objectsAreEqual(K a, K b) {
        return a == b || a != null && a.equals(b);
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void createBucket(int index, Node<K, V> node) {
        table[index] = node;
        size++;
    }

    private void updateBucket(int bucketIndex, Node<K, V> node) {
        Node<K, V> bucket = table[bucketIndex];
        while (bucket != null) {
            if (objectsAreEqual(bucket.key, node.key)) {
                bucket.value = node.value;
                return;
            }
            if (bucket.next == null) {
                bucket.next = node;
                size++;
                return;
            }
            bucket = bucket.next;
        }
    }

    private void transferDataToNewEmptyTable(Node<K, V>[] oldTable, int oldSize) {
        for (int i = 0; i < oldTable.length && oldSize > 0; i++) {
            Node<K, V> bucket = oldTable[i];
            while (bucket != null && oldSize > 0) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
                oldSize--;
            }
        }
    }

    private static class Node<K, V> {
        private V value;
        private final K key;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
