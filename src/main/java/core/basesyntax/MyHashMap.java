package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = newNode;
            size++;
        } else {
            while (true) {
                if (current.key == null ? key == null : current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    break;
                }
                current = current.next;
            }
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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
        Node<K, V>[] newTable = new Node[table.length * 2];
        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = (node.key == null) ? 0
                        : Math.abs(node.key.hashCode() % newTable.length);
                Node<K, V> next = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
    }

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
