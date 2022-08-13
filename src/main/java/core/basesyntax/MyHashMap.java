package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    static final int DEFAULT_CAPACITY = 1 << 4;
    static final float DEFAULT_RESIZE_FACTOR = 0.75f;
    Node<K, V>[] table;
    private int capacity;  // length of masiv
    private int threshold; // length for nuw length
    int size; // how much elements Node in it

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_RESIZE_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> newNode = new Node<>(hash(key), key, value);
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                }
                currentNode = currentNode.next;
            }
        }
    }


    public Node<K, V>[] resize() {
        Node<K, V>[] newTab = new Node[capacity * 2];
        return newTab;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : hash(key) % capacity;
    }


    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode());
    }


    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
