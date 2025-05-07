package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = indexFromHash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value);
                    size++;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFromHash(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
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

    private void resize() {
        size = 0;
        int newCapacity = table.length << 1;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        Node<K, V>[] oldHashTable = table;
        table = new Node[newCapacity];
        transfer(oldHashTable);
    }

    private void transfer(Node<K, V>[] oldHashTable) {
        for (Node<K, V> node : oldHashTable) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    private int indexFromHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
