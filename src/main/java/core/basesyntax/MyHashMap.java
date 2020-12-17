package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = table[hash(key) % table.length];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int hash, K key, V value) {
        int currentBucket = hash % table.length;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[currentBucket] == null) {
            table[currentBucket] = newNode;
        } else {
            Node<K, V> nextNode = table[currentBucket];
            while (nextNode.next != null || Objects.equals(nextNode.key, newNode.key)) {
                if (Objects.equals(nextNode.key, newNode.key)) {
                    nextNode.value = newNode.value;
                    return;
                }
                nextNode = nextNode.next;
            }
            nextNode.next = newNode;
        }
        size++;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        size = 0;
        threshold = (int) ((table.length * 2) * LOAD_FACTOR);
        Node<K, V>[] newNode = table;
        table = new Node[newNode.length * 2];
        for (int i = 0; i < newNode.length; i++) {
            Node<K, V> tempNode = newNode[i];
            while (tempNode != null) {
                putValue(hash(tempNode.key), tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
    }

    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
