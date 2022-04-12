package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int currentCap;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.currentCap = DEFAULT_CAPACITY;
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (LOAD_FACTOR * currentCap == size) {
            resize();
        }
        Node<K, V> bucket = new Node<>(hash(key), key, value);
        if (table[bucket.hash] == null) {
            table[bucket.hash] = bucket;
        } else {
            Node<K, V> iterator = table[bucket.hash];
            while (iterator != null) {
                if (iterator.key == key || iterator.key != null
                        && iterator.key.equals(key)) {
                    iterator.value = value;
                    return;
                }
                if (iterator.next == null) {
                    break;
                }
                iterator = iterator.next;
            }
            iterator.next = bucket;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        for (Node<K, V> bucket : table) {
            if (bucket != null) {
                if (bucket.key == key || bucket.key != null
                        && bucket.key.equals(key)) {
                    return bucket.value;
                } else if (bucket.hash == hash(key)) {
                    Node<K, V> iterator = table[bucket.hash];
                    while (iterator != null) {
                        if (iterator.key == key || iterator.key != null
                                && iterator.key.equals(key)) {
                            return iterator.value;
                        }
                        iterator = iterator.next;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        currentCap = currentCap << 1;
        Node<K, V>[] oldTable = table;
        table = new Node[currentCap];
        for (Node<K, V> bucket : oldTable) {
            if (bucket != null) {
                while (bucket != null) {
                    put(bucket.key, bucket.value);
                    bucket = bucket.next;
                }
            }
        }
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % currentCap;
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
