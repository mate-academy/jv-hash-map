package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndexByKeyHash(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current.next = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKeyHash(key);
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        }
        while (node.next != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        if (Objects.equals(node.key, key)) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size < threshold) {
            return;
        }
        size = 0;
        Node<K, V>[] previousTable = table;
        table = new Node[table.length * 2];
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
        for (Node<K, V> node : previousTable) {
            if (node == null) {
                continue;
            }
            while (node.next != null) {
                put(node.key, node.value);
                node = node.next;
            }
            put(node.key, node.value);
        }
    }

    private int getIndexByKeyHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

