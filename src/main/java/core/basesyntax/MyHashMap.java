package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int bucketIndex = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = newNode;
            size++;
        } else {
            putNewBucket(table[bucketIndex], newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
        Node<K, V> entry = table[bucketIndex];
        while (entry != null) {
            if (areKeysEqual(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean areKeysEqual(K first, K second) {
        return first == second || first != null && first.equals(second);
    }

    private int getIndex(K key) {
        return key == null ? 0 : getHash(key) % table.length;
    }

    private int getHash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private void putNewBucket(Node<K, V> entry, Node<K, V> newEntry) {
        while (entry != null) {
            if (areKeysEqual(entry.key, newEntry.key)) {
                entry.value = newEntry.value;
                break;
            }
            if (entry.next == null) {
                entry.next = newEntry;
                size++;
                break;
            }
            entry = entry.next;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
