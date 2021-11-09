package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
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

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & table.length - 1;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
