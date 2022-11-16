package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putVal(key, value, table);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key) % table.length];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (31 * 17 + Math.abs(key.hashCode()));
    }

    private void putVal(K key, V value, Node<K, V>[] tab) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int numberOfCurrentBucket = hash(key) % tab.length;
        Node<K, V> currentNode = tab[numberOfCurrentBucket];
        if (currentNode == null) {
            tab[numberOfCurrentBucket] = newNode;
            size++;
        } else if (Objects.equals(currentNode.key, key)) {
            currentNode.value = value;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = newNode;
            size++;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        transfer(oldTable, newTable);
        table = newTable;
    }

    private void transfer(Node<K, V>[] oldTable, Node<K, V>[] newTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                putVal(node.key, node.value, newTable);
                while (node.next != null) {
                    putVal(node.next.key, node.next.value, newTable);
                    node = node.next;
                }
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
