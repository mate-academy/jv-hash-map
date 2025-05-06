package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key == null ? key == null : current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            newNode.next = table[index];
            table[index] = newNode;
        }
        size++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == null ? key == null : current.key.equals(key)) {
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
        return Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
