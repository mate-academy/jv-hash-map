package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int INCREASE_INDEX = 2;
    private float loadFactor = 0.75f;
    private int size;
    private int currentCapacity = DEFAULT_INITIAL_CAPACITY;
    private Node<K,V>[] table;

    MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
        for (int i = 0; i < size; i++) {
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

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        Node<K, V>[] oldArray = table;
        if (currentCapacity * loadFactor <= size) {
            currentCapacity = currentCapacity * INCREASE_INDEX;
            table = (Node<K, V>[]) new Node[currentCapacity];
            size = 0;
            for (Node<K, V> node : oldArray) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K,V> {
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
