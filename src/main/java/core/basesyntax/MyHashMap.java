package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resizeMap();
        }
        int hash = hashCalculator(key);
        int bucket = getIndexFromKey(key);
        if (table[bucket] == null) {
            table[bucket] = new Node<K, V>(hash, key, value);
            size++;
        } else {
            Node<K, V> current = table[bucket];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            addNewNode(bucket, new Node<>(hash, key, value));
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getIndexFromKey(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private int hashCalculator(K key) {
        return key == null ? 0 : key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode();
    }

    private void addNewNode(int bucket, Node<K, V> node) {
        node.next = table[bucket];
        table[bucket] = node;
        size++;
    }

    private int getIndexFromKey(K key) {
        int hash = hashCalculator(key);
        return hash % capacity;
    }

    private void resizeMap() {
        Node<K, V>[] oldTable = table;
        int newCapacity = capacity * GROW_FACTOR;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> temp = node;
            while (temp != null) {
                Node<K, V> nextTemp = temp.next;
                addNewNode(temp.hash % newCapacity, temp);
                temp = nextTemp;
            }
        }
        capacity = newCapacity;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.value = value;
            this.key = key;
        }
    }
}
