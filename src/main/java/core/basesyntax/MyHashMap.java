package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_RESIZE_COEFFICIENT = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key,table.length);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        newNode.next = table[index];
        table[index] = newNode;
        size++;

        if ((double) size / table.length > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key, table.length)];
        while (node != null) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
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

    private int getIndex(K key, int tableLength) {
        int hashCode = (key != null)
                ? key.hashCode()
                : 0;
        int index = Math.abs(hashCode) % tableLength;
        return index;
    }

    private void resize() {
        int newCapacity = table.length * DEFAULT_RESIZE_COEFFICIENT;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = getIndex(node.key, newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }

        table = newTable;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
