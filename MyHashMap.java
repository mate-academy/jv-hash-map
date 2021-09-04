package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int RESIZE_MULTIPLY = 2;
    private Node[] table;
    private int size;
    private int threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        addNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = keyHash(key);
        int capacity = table.length;
        Node<K,V> currentNode = table[getIndex(key, capacity)];
        while (currentNode != null) {
            if (currentNode.hash == hash
                    && Objects.equals(currentNode.key, key)) {
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

    private void addNode(K key, V value) {
        int hash = keyHash(key);
        int capacity = table.length;
        int index = getIndex(key, capacity);
        Node<K,V> currentNode = table[index];
        Node<K, V> node = null;

        if (currentNode == null) {
            table[index] = new Node<>(keyHash(key), key, value, null);
        } else {
            while (currentNode != null) {
                if (currentNode.hash == hash
                        && Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                node = currentNode;
                currentNode = currentNode.next;
            }
            node.next = new Node<>(keyHash(key), key, value, null);
        }
        size++;
    }

    private int keyHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(K key, int capacity) {
        int hash = keyHash(key) % capacity;
        return hash < 0 ? hash * -1 : hash;
    }

    private boolean resize() {
        if (size == threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * RESIZE_MULTIPLY];
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            size = 0;
            for (Node<K, V> kvNode : oldTable) {
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
            return true;
        }
        return false;
    }
}
