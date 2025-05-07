package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = (table.length - 1) & hash(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (current == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prev = null;
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> current : table) {
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        threshold <<= 1;
        table = (Node<K, V>[]) new Node[oldTable.length << 1];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        int hash;
        return key == null ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
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
