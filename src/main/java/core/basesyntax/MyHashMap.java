package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = findNodeIndex(key, table.length);
        Node<K, V> current = table[index];

        if (current == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K,V> prev = null;
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = new Node<>(key, value, null);
        }
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[findNodeIndex(key, table.length)];
        if (node != null) {
            do {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
            } while ((node = node.next) != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (thresholdNotExceeded()) {
            return;
        }

        Node<K, V>[] newTable = new Node[table.length * GROW_FACTOR];

        int i = 1;
        Node<K, V> current;
        Node<K, V> next = table[0];

        while (i < table.length) {
            current = next;

            if (current == null) {
                next = table[i++];
                continue;
            } else {
                next = current.next;
            }

            int newIndex = findNodeIndex(current.key, newTable.length);
            if (newTable[newIndex] == null) {
                newTable[newIndex] = current;
            } else {
                findListLastNode(newTable[newIndex]).next = current;
            }
            current.next = null;
        }
        table = newTable;
    }

    private int findNodeIndex(K key, int capacity) {
        return Math.abs(Objects.hashCode(key) % capacity);
    }

    private Node<K,V> findListLastNode(Node<K,V> node) {
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    private boolean thresholdNotExceeded() {
        return size < table.length * LOAD_FACTOR;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.hash = Objects.hashCode(key);
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
