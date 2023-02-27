package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREASE_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkStorageSize();
        Node<K,V> currentNode;
        Node<K,V> lastNode;
        int index = getIndex(key);
        if (table[index] != null) {
            currentNode = table[index];
            lastNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                lastNode = currentNode;
                currentNode = currentNode.next;
            }
            lastNode.next = new Node<>(key, value);
            size++;
            return;
        }
        table[index] = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];
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

    private void checkStorageSize() {
        if (size == table.length * LOAD_FACTOR) {
            Node<K,V> [] temporary = table;
            table = new Node[table.length * INCREASE_FACTOR];
            size = 0;
            Node<K,V> currentNode;
            for (Node<K,V> node : temporary) {
                currentNode = node;
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
