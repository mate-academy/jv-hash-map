package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private int size;
    private Node<K, V>[] elements;

    public MyHashMap() {
        elements = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        int index = hash(node.key);
        if (size > elements.length * LOAD_FACTOR) {
            resize();
        }
        if (elements[index] == null) {
            elements[index] = node;
            size++;
        } else {
            Node<K, V> temp = elements[index];
            while (temp != null) {
                if (Objects.equals(node.key, temp.key)) {
                    temp.value = value;
                    return;
                }
                if (temp.next == null) {
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
        return (key == null) ? 0 : Math.abs(h = key.hashCode()) % elements.length;
    }

    private void resize() {
        Node<K, V>[] oldElements = elements;
        elements = new Node[elements.length * 2];
        size = 0;
        for (Node<K, V> node : oldElements) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
