package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);

        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        if (size >= DEFAULT_LOAD_FACTOR * table.length) {
            resize(table.length * RESIZE_MULTIPLIER);
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
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

    private int getIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize(int capacity) {
        Node<K,V>[] tempTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K,V> node : tempTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (obj.getClass().equals(getClass())) {
                Node<K, V> current = (Node<K, V>) obj;
                return Objects.equals(this.key, current.key)
                        && Objects.equals(this.value, current.value);
            }
            return false;
        }
    }
}
