package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hashIndex(key, table.length);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            resizeIfNeeded();
            return;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        int index = hashIndex(key, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key,key)) {
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

    private int hashIndex(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode()) % length;
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded() {
        if (size > table.length * LOAD_FACTOR) {
            int newCapacity = table.length << 1;
            Node<K, V>[] newTable = new Node[newCapacity];
            for (Node<K, V> kvNode : table) {
                Node<K, V> node = kvNode;
                while (node != null) {
                    K key = node.key;
                    V value = node.value;
                    int newIndex = hashIndex(key, newCapacity);
                    Node<K, V> newNode = new Node<>(key, value, newTable[newIndex]);
                    newTable[newIndex] = newNode;
                    node = node.next;
                }
            }
            table = newTable;
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
