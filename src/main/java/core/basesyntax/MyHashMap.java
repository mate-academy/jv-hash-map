package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] array;
    private int threshhold;
    private int size;

    public MyHashMap() {
        array = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        threshhold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >> 16);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        put(node, getIndex(node));
        if (size > threshhold) {
            resize();
        }
    }

    private void put(Node<K, V> newNode, int index) {
        if (array[index] == null) {
            array[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = array[index];
        while (!Objects.equals(node.key, newNode.key) && node.next != null) {
            node = node.next;
        }
        if (Objects.equals(newNode.key, node.key)) {
            node.value = newNode.value;
            return;
        }
        node.next = newNode;
        size++;
    }

    private void resize() {
        int newCapacity = array.length * 2;
        size = 0;
        threshhold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] tempArray = array;
        array = (Node<K,V>[]) new Node[newCapacity];
        for (Node<K, V> node : tempArray) {
            while (node != null) {
                put(new Node<>(node), getIndex(node));
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = array[getIndex(key)];
        if (node == null) {
            return null;
        }
        while (!Objects.equals(node.key, key) && node.next != null) {
            node = node.next;
        }
        if (Objects.equals(node.key, key)) {
            return node.value;
        }
        return null;
    }

    public int getIndex(Node<K, V> node) {
        return node == null ? 0 : node.hash % array.length;
    }

    public int getIndex(K key) {
        return key == null ? 0 : hash(key) % array.length;
    }

    private static class Node<K, V> {
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

        public Node(Node<K, V> node) {
            hash = node.hash;
            key = node.key;
            value = node.value;
            next = null;
        }
    }
}
