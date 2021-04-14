package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_SIZE = 2;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] container;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        container = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            increaseSize();
        }
        int index = getIndexUsingHashcode(key);
        Node<K, V> currentNode = container[index];

        while (currentNode != null){
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        container[index] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> lookingNode = container[key == null ? 0 : getIndexUsingHashcode(key)];
        while (lookingNode != null) {
            if (lookingNode.key == key || lookingNode.key != null && lookingNode.key.equals(key)) {
                return lookingNode.value;
            }
            lookingNode = lookingNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void increaseSize() {
        capacity *= INCREASE_SIZE;
        threshold *= INCREASE_SIZE;
        size = 0;
        Node<K, V>[] oldContainer = container;
        container = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> node : oldContainer) {
            Node<K, V> temp = node;
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
    }

    private int getIndexUsingHashcode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }
}
