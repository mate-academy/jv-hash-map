package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value, hash);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> entry = table[index];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return hash(key) & table.length - 1;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        if ((float) size / table.length >= DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] newTable = new Node[(table.length * MULTIPLIER)];
            table = transfer(newTable);
        }
    }

    private Node<K, V>[] transfer(Node<K, V>[] newTable) {
        for (Node<K, V> kvNode : table) {
            Node<K, V> node = kvNode;
            while (node != null) {
                Node<K, V> next = node.next;
                int hash = hash(node.key);
                int index = hash & newTable.length - 1;
                Node<K, V> newNode = new Node<>(node.key, node.value, hash);
                newNode.next = newTable[index];
                newTable[index] = newNode;
                node = next;
            }

        }
        return newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
