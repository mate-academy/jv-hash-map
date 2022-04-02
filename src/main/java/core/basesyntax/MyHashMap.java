package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private static final int DEFAULT_CAPACITY = 1 << 4; // 16
    private int size;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int capacity) {
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> entry = new Node<>(key, value, null);
        int bucket = getHash(key) % DEFAULT_CAPACITY;
        Node<K, V> existing = table[bucket];
        if (existing == null) {
            table[bucket] = entry;
            size++;
        } else {
            while (existing.next != null) {
                if (existing.key.equals(key)) {
                    existing.value = value;
                    return;
                }
                existing = existing.next;
            }

            if (existing.key.equals(key)) {
                existing.value = value;
            } else {
                existing.next = entry;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> bucket = table[getHash(key) % DEFAULT_CAPACITY];

        while (bucket != null) {
            if (bucket.key.equals(key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode());
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
