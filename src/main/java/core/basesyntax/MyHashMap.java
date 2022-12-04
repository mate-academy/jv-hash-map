package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private float threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = DEFAULT_LOAD_FACTOR * INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int indexOfBucket = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[indexOfBucket];
        while (currentNode.next != null && !Objects.equals(key,currentNode.key)) {
            currentNode = currentNode.next;
        }
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            return;
        }
        currentNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newSize = 2 * table.length;
        threshold = newSize * DEFAULT_LOAD_FACTOR;
        Node<K, V>[] oldTable = table;
        table = new Node[newSize];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            //this.next = next;
        }
    }
}
