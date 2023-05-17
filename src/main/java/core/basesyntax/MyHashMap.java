package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
        this.capacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K,V> current = table[index];
        Node<K,V> prev = null;

        while (current != null) {
            if (key == null && current.key == null) {
                current.value = value;
                return;
            } else if (key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }

        Node<K,V> newNode = new Node<>(key, value);
        if (prev == null) {
            table[index] = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
        if (size >= (capacity * LOAD_FACTOR)) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (key == null && node.key == null) {
                return node.value;
            } else if (key != null && key.equals(node.key)) {
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

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    public void resize() {
        capacity *= 2;
        Node<K,V>[] newTable = new Node[capacity];
        for (Node<K,V> node : table) {
            while (node != null) {
                int index = getIndex(node.key);
                Node<K,V> temp = node.next;
                node.next = newTable[index];
                newTable[index] = node;
                node = temp;
            }
        }
        table = newTable;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
