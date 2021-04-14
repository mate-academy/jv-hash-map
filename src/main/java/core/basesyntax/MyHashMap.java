package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> current = new Node<>(key, value, null);
        Node<K, V> nodeInTable = table[index];
        if (table[index] == null) {
            table[index] = current;
        } else {
            while (nodeInTable.next != null || Objects.equals(nodeInTable.key, key)) {
                if (Objects.equals(nodeInTable.key, key)) {
                    nodeInTable.value = value;
                    return;
                }
                nodeInTable = nodeInTable.next;
            }
            nodeInTable.next = current;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
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

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshold = (int)(table.length * DEFAULT_LOAD_FACTOR);
        moveBuckets(oldTable);
    }

    private void moveBuckets(Node<K, V>[] oldTable) {
        for (Node<K, V> current : oldTable) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
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
