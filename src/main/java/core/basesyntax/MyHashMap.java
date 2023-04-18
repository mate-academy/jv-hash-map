package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private static final int RESIZE_MULTIPLY = 2;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
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
        int hash = hash(key);
        Node<K,V> currentNode = table[getIndex(key)];
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
        int index = getIndex(key);
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = node;
        size++;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size < table.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_MULTIPLY];
        size = 0;
        for (Node<K, V> kvNode : oldTable) {
            while (kvNode != null) {
                put(kvNode.key, kvNode.value);
                kvNode = kvNode.next;
            }
        }
    }
}
