package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int CAPACITY_INCREASE = 2;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = findIndex(key);
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
        }
        table[index] = newNode;
        if (++size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * CAPACITY_INCREASE;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = transfer(newTable);
    }

    private Node<K, V>[] transfer(Node<K, V>[] transferTable) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> transferNode = table[i];
            while (transferNode != null) {
                Node<K, V> next = transferNode.next;
                int newIndex = (transferNode.key == null) ? 0
                        : Math.abs(transferNode.key.hashCode() % transferTable.length);
                transferNode.next = transferTable[newIndex];
                transferTable[newIndex] = transferNode;
                transferNode = next;
            }
        }
        return transferTable;
    }

    private int findIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
