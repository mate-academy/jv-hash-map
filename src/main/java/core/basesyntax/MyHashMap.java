package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
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

    @Override
    public void put(K key, V value) {
        Node<K, V> input = new Node<>(key, value, null);
        int indexForPut = calculateIndex(key);
        if (size >= threshold) {
            changeSize();
        }
        if (table[indexForPut] == null) {
            table[indexForPut] = input;
            size++;
        } else {
            Node<K, V> currentNode = table[indexForPut];
            do {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = input;
                    size++;
                    return;
                } else {
                    currentNode = currentNode.next;
                }
            } while (true);
        }
    }

    @Override
    public V getValue(K key) {
        int indexForGet = calculateIndex(key);
        Node<K, V> currentNode = table[indexForGet];
        if (Objects.equals(currentNode, null)) {
            return null;
        }
        if (currentNode.next == null) {
            return currentNode.value;
        }
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

    private int calculateIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void changeSize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        table = new Node[(oldCapacity * CAPACITY_RESIZE_FACTOR)];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}

