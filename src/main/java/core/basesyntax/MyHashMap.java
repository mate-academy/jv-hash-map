package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = 12;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int hashKey = getHash(key);
        Node<K, V> currentNode = table[hashKey];
        if (table[hashKey] == null) {
            table[hashKey] = newNode;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                currentNode = currentNode.next;
            }
            newNode.next = table[hashKey];
            table[hashKey] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexKey = getHash(key);
        Node<K, V> currentNode = table[indexKey];
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs((key.hashCode() * 17 % table.length));
    }

    private void resize() {
        if (threshold == size) {
            threshold = (int) (table.length * LOAD_FACTOR);
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * CAPACITY_MULTIPLIER];
            size = 0;
            for (Node<K, V> element : oldTable) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    private class Node<K, V> {
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
