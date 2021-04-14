package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            table = resize();
        }
        int index = getIndex(key);
        Node<K, V> current = new Node<>(key, value, null);
        Node<K, V> nodeInTable = table[index];
        if (table[index] == null) {
            table[index] = current;
            size++;
        } else {
            while (nodeInTable.next != null || Objects.equals(nodeInTable.key, key)) {
                if (Objects.equals(nodeInTable.key, key)) {
                    nodeInTable.value = value;
                    return;
                }
                nodeInTable = nodeInTable.next;
            }
            nodeInTable.next = current;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        if (current == null) {
            return null;
        }
        while (current.next != null || Objects.equals(current.key, key)) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> [] resize() {
        final Node<K,V>[] oldTab = table;
        Node<K, V> current;
        table = new Node[capacity * 2];
        capacity = table.length;
        threshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (int i = 0; i < oldTab.length; i++) {
            current = oldTab[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        return table;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;

        }
    }
}
