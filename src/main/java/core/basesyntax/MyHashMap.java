package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final int HASH_MASK = 0x7fffffff;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int capacity = DEFAULT_CAPACITY;
    private final double loadFactor = 0.75;

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : key.hashCode();
        Node<K, V> node = new Node<>(hash, key, value, null);

        if (size >= capacity * loadFactor) {
            resize();
        }

        int index = (hash & HASH_MASK) % capacity;

        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }

        Node<K, V> curr = table[index];

        while (curr != null) {
            if (key == null ? curr.key == null : key.equals(curr.key)) {
                curr.value = value;
                return;
            }

            if (curr.next == null) {
                curr.next = node;
                size++;
                return;
            }
            curr = curr.next;
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        int index = (hash & HASH_MASK) % capacity;
        Node<K, V> curr = table[index];
        while (curr != null) {
            if (key == null ? curr.key == null : key.equals(curr.key)) {
                return curr.value;
            }
            curr = curr.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        capacity = capacity * RESIZE_MULTIPLIER;

        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        table = newTable;
        size = 0;

        for (Node<K, V> record : oldTable) {
            if (record != null) {
                Node<K, V> curr = record;
                while (curr != null) {
                    put(curr.key, curr.value);
                    curr = curr.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

