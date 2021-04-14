package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
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

    private void resize() {
        final Node<K,V>[] oldTab = table;

        table = new Node[table.length * 2];
        threshold = (int)(table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        moveBuckets(oldTab);
    }

    private void moveBuckets(Node<K, V>[] oldTable) {
        Node<K, V> current;
        for (Node<K, V> kvNode : oldTable) {
            current = kvNode;
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
