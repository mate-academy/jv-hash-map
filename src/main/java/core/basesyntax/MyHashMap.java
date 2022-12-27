package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> { // test commit
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private Node<K, V> currentNode;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= Math.abs(table.length * LOAD_FACTOR)) {
            resize();
        }
        int index = findIndex(key);
        currentNode = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        currentNode = table[findIndex(key)];
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

    private int findIndex(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        return key == null ? 0 : (Math.abs(key.hashCode() * 17));
    }

    private void resize() {
        Node<K, V>[] array = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> element : array) {
            currentNode = element;
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }
}
