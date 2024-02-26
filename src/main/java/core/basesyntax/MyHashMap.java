package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void putValue(K key, V value) {
        Node<K, V> current = table[getIndex(key)];
        Node<K, V> nodeNew = new Node<>(key, value);
        while (current != null) {
            if (Objects.equals(current.key, nodeNew.key)) {
                current.value = nodeNew.value;
                return;
            } else if (current.next == null) {
                current.next = nodeNew;
                ++size;
                return;
            }
            current = current.next;
        }
        table[getIndex(key)] = nodeNew;
        ++size;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshold *= 2;
        size = 0;
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                while (kvNode != null) {
                    putValue(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

