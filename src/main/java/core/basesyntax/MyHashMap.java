package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        resizeCapacity();
        Node<K, V> node = new Node<>(key, value);
        int index = indexOf(node.hash);
        if (table[index] == null) {
            createBucket(index, node);
        } else {
            updateBucket(index, node);
        }
    }

    @Override
    public V getValue(K key) {
        int keyHash = hash(key);
        int index = indexOf(keyHash);
        if (table[index] != null) {
            Node<K, V> bucket = table[index];
            while (bucket != null) {
                if (bucket.hash == keyHash && objectsAreEqual(bucket.key, key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
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
        if (size < threshold) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * CAPACITY_MULTIPLIER];
        int oldSize = size;
        size = 0;
        threshold *= CAPACITY_MULTIPLIER;
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
            if (bucket.hash == node.hash && objectsAreEqual(bucket.key, node.key)) {
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

    private class Node<K, V> {
        private final int hash;
        private V value;
        private final K key;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash(key);
        }
    }
}
