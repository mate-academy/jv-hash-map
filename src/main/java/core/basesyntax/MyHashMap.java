package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30; // ~1B entries
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (table == null || size >= threshold) {
            resize();
        }
        int index = indexFor(hash(key));
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (true) {
            if ((key == null && node.key == null) || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int index = indexFor(hash(key));
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == null && node.key == null) || (node.key != null && node.key.equals(key))) {
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

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int indexFor(int hash) {
        return hash & (capacity - 1);
    }

    private void resize() {
        int newCapacity = (table == null) ? DEFAULT_CAPACITY : capacity * 2;
        if (newCapacity > MAX_CAPACITY) {
            newCapacity = MAX_CAPACITY;
        }
        capacity = newCapacity;
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        if (oldTable != null) {
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
