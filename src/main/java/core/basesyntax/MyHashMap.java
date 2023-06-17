package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;

    private class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        setInitialCapacity();
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(node.key);
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> newNode = table[index];
            while (newNode != null) {
                if (Objects.equals(node.key, newNode.key)) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = node;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void resize() {
        Node<K, V>[] previousTable = table;
        table = new Node[table.length * RESIZE_FACTOR];
        size = 0;
        for (Node<K, V> node : previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void setInitialCapacity() {
        if (table == null) {
            table = new Node[DEFAULT_CAPACITY];
        }
    }
}

