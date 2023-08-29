package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final double DEFAULT_LOAD_FACTOR = 0.75;
    static final int CAPACITY_MODIFIER = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = Math.abs(hash) % table.length;
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            newNode.next = table[index];
            table[index] = newNode;
        }
        size++;
        if (size == threshold) {
            increaseCapacity(table.length);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = Math.abs(hash) % table.length;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key,currentNode.key)) {
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

    private void increaseCapacity(int oldCapacity) {
        int newCapacity = oldCapacity * CAPACITY_MODIFIER;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int newIndex = Math.abs(hash(oldNode.key)) % newCapacity;
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private int hash(K key) {
        int result = 17;
        result += 31 * (key == null ? 0 : key.hashCode());
        return result;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}



