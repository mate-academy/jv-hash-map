package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final float INCREASE_FACTOR = 1.5f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resizeTable();
        }

        int hash = getHash(key);
        Node<K, V> putted = new Node<>(hash, key, value);
        Node<K, V> prev = table[hash % capacity];
        if (prev != null) {
            while (prev.next != null) {
                prev = prev.next;
            }
            prev.next = putted;
        } else {
            table[hash % capacity] = putted;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getHash(key) % capacity];
        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeTable() {
        Node<K, V>[] oldTable = (Node<K, V>[]) new Node[capacity];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        capacity = (int) (capacity * INCREASE_FACTOR);
        threshold = (int) (capacity * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> current : table) {
            if (current != null) {
                put(current.key, current.value);
                while (current.next != null) {
                    current = current.next;
                    put(current.key, current.value);
                }
            }
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final int hashKey;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hashKey, K key, V value) {
            this.hashKey = hashKey;
            this.key = key;
            this.value = value;
        }
    }
}
