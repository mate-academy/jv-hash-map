package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int MULTIPLAYER_CAPACITY = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private LinkedList<Node<K, V>>[] table;
    private int size;

    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
    }

    @Override
    public void node() {
    }

    @Override
    public void put(K key, V value) {
        int index = getHashIndex(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        for (Node<K, V> entry : table[index]) {
            if (entry.key == null && key == null) {
                entry.value = value;
                return;
            } else if (entry.key == null) {
                continue;
            } else if ((entry.key.equals(key))) {
                entry.value = value;
                return;
            }
        }
        table[index].add(new Node<>(key, value));
        size++;
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(key);
        LinkedList<Node<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Node<K, V> entry : bucket) {
                if (entry.key == null && key == null) {
                    return entry.value;
                } else if (entry.key == null) {
                    continue;
                } else if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashIndex(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = key.hashCode() % table.length;
            if (index < 0) {
                index *= -1;
            }
        }
        return index;
    }

    private void resize() {
        int newCapacity = table.length * MULTIPLAYER_CAPACITY;
        LinkedList<Node<K, V>>[] newBuckets = new LinkedList[newCapacity];
        for (LinkedList<Node<K, V>> bucket : table) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = Math.abs(node.key.hashCode() % newCapacity);
                    LinkedList<Node<K, V>> newBucket = newBuckets[newIndex];
                    if (newBucket == null) {
                        newBucket = new LinkedList<>();
                        newBuckets[newIndex] = newBucket;
                    }
                    newBucket.add(node);
                }
            }
        }
        table = newBuckets;
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
