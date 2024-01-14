package core.basesyntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int MULTIPLAYER_CAPACITY = 2;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private List<Node<K, V>>[] table;
    private int size;

    public MyHashMap() {
        table = new List[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
        int index = getHashIndex(key);
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }
        for (Node<K, V> node : table[index]) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
        }
        table[index].add(new Node<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(key);
        List<Node<K, V>> bucket = table[index];
        if (bucket != null) {
            for (Node<K, V> entry : bucket) {
                if (Objects.equals(entry.key, key)) {
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
        List<Node<K, V>>[] newTable = new List[newCapacity];

        for (List<Node<K, V>> bucket : table) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = Math.abs(node.key.hashCode() % newCapacity);
                    List<Node<K, V>> newBucket = newTable[newIndex];
                    if (newBucket == null) {
                        newBucket = new LinkedList<>();
                        newTable[newIndex] = newBucket;
                    }
                    newBucket.add(node);
                }
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
