package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float RESIZE_THRESHOLD = 0.75f;
    private static final float RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (putIntoTable(key, value)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void resize() {
        if (size / (float) table.length >= RESIZE_THRESHOLD) {
            Node<K, V>[] temp = table;
            table = new Node[(int) (temp.length * RESIZE_MULTIPLIER)];
            for (Node<K, V> node : temp) {
                while (node != null) {
                    putIntoTable(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean putIntoTable(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            return true;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode.next != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return false;
            }
            currentNode = currentNode.next;
        }
        if (Objects.equals(currentNode.key, key)) {
            currentNode.value = value;
            return false;
        }
        currentNode.next = newNode;
        return true;
    }

    private int calculateIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
