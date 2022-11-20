package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_STEP = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
        int index = getIndexForHash(newNode.hash);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (!Objects.equals(currentNode.key, key) && currentNode.next != null) {
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndexForHash(getHash(key))];
        while (node != null && !Objects.equals(node.key, key)) {
            node = node.next;
        }
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndexForHash(int hash) {
        return Math.abs(hash % table.length);
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * RESIZE_STEP];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> node = new Node<>(oldNode.hash, oldNode.key, oldNode.value, null);
                int index = Math.abs(node.hash % newTable.length);
                if (newTable[index] == null) {
                    newTable[index] = node;
                } else {
                    Node<K, V> currentNode = newTable[index];
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                    }
                    currentNode.next = node;
                }
                oldNode = oldNode.next;
            }
        }
        table = newTable;
    }
}
