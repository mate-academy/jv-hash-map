package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndex(key)];
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

    private void resizeIfNeeded() {
        if (size < table.length * DEFAULT_LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTab) {
            if (node != null) {
                while (node != null) {
                    putValue(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private void putValue(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (current != null) {
            while (current != null) {
                if (Objects.equals(current.key, newNode.key)) {
                    current.value = newNode.value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                } else {
                    current = current.next;
                }
            }
        } else {
            table[index] = newNode;
            size++;
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
