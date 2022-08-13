package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int INCREASE_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        int index = getNodeIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value);
            size++;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = table[getNodeIndex(key)];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * INCREASE_FACTOR];
        size = 0;
        for (Node<K, V> element : oldTable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;

            }
        }
    }

    private int getNodeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
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
