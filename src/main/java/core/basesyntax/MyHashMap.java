package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K,V>[] table = new Node[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        resizeCheck();
        int index = getIndex(key);
        Node<K, V> node = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> kvNode = findNode(index, key);
        if (kvNode != null) {
            kvNode.value = value;
            return;
        }
        for (Node<K,V> i = table[index]; i != null; i = i.next) {
            if (i.next == null) {
                i.next = node;
                size++;
                return;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(getIndex(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeCheck() {
        if (size > table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * GROW_FACTOR];
            size = 0;
            for (Node<K, V> kvNode : oldTable) {
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return Math.abs(Objects.hashCode(key) % table.length);
    }

    private Node<K,V> findNode(int index, K key) {
        Node<K,V> node = new Node<>(key, null);
        for (Node<K,V> i = table[index]; i != null; i = i.next) {
            if (i.key == null && key == null || node.key != null && i.key != null
                    && node.key.hashCode() == i.key.hashCode() && node.key.equals(i.key)) {
                return i;
            }
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
