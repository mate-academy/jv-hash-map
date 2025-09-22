package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;

    private LinkedList<Node<K, V>>[] list;
    private int capacity;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        list = (LinkedList<Node<K, V>>[]) new LinkedList[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        extendCapacity();

        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;

        if (list[index] == null) {
            list[index] = new LinkedList<>();
        }

        for (Node<K, V> node : list[index]) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
        }

        list[index].add(new Node<>(key, value));
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;

        if (list[index] == null) {
            return null;
        }

        for (Node<K, V> node : list[index]) {
            if ((node.key == null && key == null) || (node.key != null && node.key.equals(key))) {
                return node.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    // Private helper method should go after public ones
    private void extendCapacity() {
        if (size >= capacity * LOAD_FACTOR) {
            int newCapacity = capacity * RESIZE_MULTIPLIER;
            LinkedList<Node<K, V>>[] newList = new LinkedList[newCapacity];

            for (LinkedList<Node<K, V>> bucket : list) {
                if (bucket != null) {
                    for (Node<K, V> node : bucket) {
                        int newIndex = (node.key == null)
                                ? 0
                                : Math.abs(node.key.hashCode()) % newCapacity;
                        if (newList[newIndex] == null) {
                            newList[newIndex] = new LinkedList<>();
                        }
                        newList[newIndex].add(node);
                    }
                }
            }

            list = newList;
            capacity = newCapacity;
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
