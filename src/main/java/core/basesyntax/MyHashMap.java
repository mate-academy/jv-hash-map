package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;

    private K key;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
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
        int keyIndex = indexInArray(key);
        Node<K, V> node = table[keyIndex];
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

    private void putValue(K key, V value) {
        int index = indexInArray(key);
        Node<K, V> node = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    private void resize() {
        int lengthTable = table.length << 1;
        threshold = (int) (DEFAULT_LOAD_FACTOR * lengthTable);
        Node<K, V>[] oldTable = table;
        table = new Node[lengthTable];
        size = 0;
        for (Node<K, V> elemetNode : oldTable) {
            while (elemetNode != null) {
                put(elemetNode.key, elemetNode.value);
                elemetNode = elemetNode.next;
            }
        }
    }

    private int indexInArray(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
