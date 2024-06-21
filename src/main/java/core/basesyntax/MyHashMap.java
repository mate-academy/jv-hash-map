package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int ARRAY_SIZE = 16;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[ARRAY_SIZE];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node = new Node<>(key, value, hash, null);
        node.next = table[index];
        table[index] = node;
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        if ((float) size / table.length >= DEFAULT_LOAD_FACTOR) {
            Node[] newTable = (Node<K, V>[]) new Node[(table.length * MULTIPLIER)];
            for (int i = 0; i < table.length; i++) {
                Node<K, V> node = table[i];
                while (node != null) {
                    int newIndex = node.hash & (newTable.length - 1);
                    Node<K, V> next = node.next;
                    node.next = newTable[newIndex];
                    newTable[newIndex] = node;
                    node = next;
                }
            }
            table = newTable;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        private Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
