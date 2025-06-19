package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        putVal(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndex(key)];
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
        if (size < table.length * DEFAULT_LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    putVal(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private void putVal(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value);

        if (current == null) {
            table[index] = newNode;
            size++;
            return;
        }
        while (current != null) {
            if (Objects.equals(current.key, newNode.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }

        current.next = newNode;
        size++;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs((key.hashCode() % table.length));
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
