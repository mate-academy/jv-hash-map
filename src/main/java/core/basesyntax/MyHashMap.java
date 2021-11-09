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
        if (size >= threshold) {
            resize();
        }
        int index = findIndexByKey(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        } else {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[findIndexByKey(key)];
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

    private void resize() {
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTab = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int findIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K,V> {
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
