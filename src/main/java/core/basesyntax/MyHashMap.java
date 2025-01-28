package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int capacity;
    private V nullKeyValue;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) { // Handle null key
            nullKeyValue = value;
            return;
        }
        if ((float) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(key, value, table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nullKeyValue;
        }

        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size + (nullKeyValue != null ? 1 : 0);
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
