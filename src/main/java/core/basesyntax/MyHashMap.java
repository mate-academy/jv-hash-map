package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> current = table[hash(key)];
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(hash(key), key, value, null);
                size++;
                return;
            }
            current = current.next;
        }
        table[hash(key)] = new Node<>(hash(key), key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[hash(key)];
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold *= 2;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode() % table.length);
        }
    }

    private static class Node<K,V> {
        private int hash;
        private K key;
        private Node<K, V> next;
        private V value;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
