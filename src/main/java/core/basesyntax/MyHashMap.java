package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[index] = new Node<>(key, value, table[index]);
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * CAPACITY_MULTIPLIER];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int index = getIndex(node.key);
                node.next = table[index];
                table[index] = node;
                node = nextNode;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
