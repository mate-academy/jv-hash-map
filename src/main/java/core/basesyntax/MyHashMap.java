package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private int capacity;
    private final double loadFactor;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, double loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (table[index] != null) {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            Node<K, V> lastNode = getLastNode(table[index]);
            lastNode.next = newNode;
        } else {
            table[index] = newNode;
        }

        size++;
        if (size + 1 > capacity * loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (key.equals(current.key)) {
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private Node<K, V> getLastNode(Node<K, V> node) {
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    private void putForNullKey(V value) {
        int index = 0;
        Node<K, V> newNode = new Node<>(null, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key == null) {
                current.value = value;
            } else {
                current.next = newNode;
                size++;
            }
        }
    }

    private V getForNullKey() {
        Node<K, V> current = table[0];
        while (current != null) {
            if (current.key == null) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] newTable = new Node[capacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = getIndex(node.key);
                Node<K, V> temp = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = temp;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
