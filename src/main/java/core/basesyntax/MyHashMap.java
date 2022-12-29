package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOADFACTOR = 0.75d;
    private int currentCapacity;
    private int currentThreshold;
    private int size;
    private Node<K, V>[] elements;

    public MyHashMap() {
        this.elements = new Node[DEFAULT_CAPACITY];
        currentCapacity = DEFAULT_CAPACITY;
        currentThreshold = (int) (currentCapacity * DEFAULT_LOADFACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        int index = hash(node.key);
        Node<K, V> temp = elements[index];
        if (size > currentThreshold) {
            resize();
        }
        if (temp == null) {
            elements[index] = node;
            size++;
        } else {
            while (temp != null) {
                if (Objects.equals(node.key, temp.key)) {
                    temp.value = node.value;
                    return;
                } else if (temp.next == null) {
                    temp.next = node;
                    size++;
                    return;
                }
                temp = temp.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> temp = elements[index];
        while (temp != null) {
            if (Objects.equals(temp.key, key)) {
                return temp.value;
            } else {
                temp = temp.next;
            }
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

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs(h = key.hashCode()) % currentCapacity;
    }

    private void resize() {
        currentCapacity = currentCapacity * 2;
        currentThreshold = currentThreshold * 2;
        Node<K, V>[] temp = elements;
        elements = (Node<K, V>[]) new Node[currentCapacity];
        size = 0;
        for (Node<K, V> node : temp) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
