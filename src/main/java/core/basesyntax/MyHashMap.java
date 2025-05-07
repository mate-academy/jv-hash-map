package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> current = table[index];
        Node<K, V> tmp = null;
        while (current != null) {
            if (current.key == node.key
                    || current.key != null && current.key.equals(node.key)) {
                current.value = node.value;
                return;
            }
            if (current.next == null) {
                tmp = current;
            }
            current = current.next;
        }
        current = node;
        tmp.next = current;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> current = table[index];
        while (current != null) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
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

    @Override
    public String toString() {
        return Arrays.toString(table);
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Node<K,V>[] currentTable = table;
        table = new Node[table.length * INCREASE_COEFFICIENT];
        size = 0;
        Node<K, V> current;
        for (int i = 0; i < table.length / 2; i++) {
            current = currentTable[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
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
