package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY;
    private static final float DEFAULT_LOAD_FACTOR;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    static {
        DEFAULT_INITIAL_CAPACITY = 16;
        DEFAULT_LOAD_FACTOR = 0.75f;
    }

    {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> firstNode = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (firstNode == null) {
            table[index] = newNode;
            resize();
            return;
        }
        addOrReplace(firstNode, newNode);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> firstNode = table[index];
        for (Node<K, V> node = firstNode; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addOrReplace(Node<K, V> firstNode, Node<K, V> newNode) {
        Node<K, V> node = new Node<>(null, null);
        node.next = firstNode;
        do {
            node = node.next;
            if (Objects.equals(node.key, newNode.key)) {
                node.value = newNode.value;
                return;
            }
        } while (node.next != null);
        node.next = newNode;
        resize();
    }

    private void resize() {
        if (++size > threshold()) {
            grow();
        }
    }

    private void grow() {
        capacity = capacity << 1;
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[capacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> bucket : oldTable) {
            if (bucket != null) {
                for (Node<K, V> node = bucket; node != null; node = node.next) {
                    put(node.key, node.value);
                }
            }
        }
    }

    private int calculateIndex(K key) {
        return hash(key) & (capacity - 1);
    }

    private float threshold() {
        return capacity * DEFAULT_LOAD_FACTOR;
    }

    private int hash(Object key) {
        return (key == null ? 0 : key.hashCode());
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
