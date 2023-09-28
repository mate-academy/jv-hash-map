package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[capacity];
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            extendCapacity();
        }
        int keyHashCode = getKeyHashCode(key);
        int index = keyHashCode % capacity;
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, keyHashCode, value, null);
            size++;
        }
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                node.value = value;
                break;
            }
            if (node.next == null) {
                node.next = new Node<>(key, keyHashCode, value, null);
                size++;
                break;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getKeyHashCode(key) % capacity];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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

    private void extendCapacity() {
        Node<K, V>[] oldTable = new Node[capacity];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        table = new Node[capacity *= 2];
        threshold = (int) (capacity * LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                size--;
                put(node.key, node.value);
                node = node.next;

            }
        }
    }

    private int getKeyHashCode(K key) {
        return key == null ? 0 : (key.hashCode() > 0 ? key.hashCode() : key.hashCode() * -1);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, int hash, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
