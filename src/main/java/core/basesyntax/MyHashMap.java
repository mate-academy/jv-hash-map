package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_GROW_FACTOR = 2;
    private static final int INITIAL_CAPACITY = 16;
    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private Node<K,V>[] table = new Node[capacity];

    @Override
    public void put(K key, V value) {
        int hash = hash(key);

        Node<K, V> current = table[hash];
        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        if (size >= DEFAULT_LOAD_FACTOR * capacity) {
            while (size >= DEFAULT_LOAD_FACTOR * capacity) {
                capacity = capacity * DEFAULT_GROW_FACTOR;
            }
            resize(capacity);
        }
        Node<K, V> newNode = new Node<K, V>(key, value, table[hash]);
        table[hash] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> current = table[hash];
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

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize(int capacity) {
        Node<K,V>[] tempTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K,V> node : tempTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
