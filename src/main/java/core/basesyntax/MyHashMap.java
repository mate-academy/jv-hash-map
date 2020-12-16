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
        int keyHash = hash(key);
        int index = keyHash % array.length;
        Node<K, V> node = new Node<>(keyHash, key, value, null);
        put(array, node, index);
        if (size > threshhold) {
            resize();
        }
    }

    private void put(Node<K, V>[] toArray, Node<K, V> newNode, int index) {
        if (toArray[index] == null) {
            toArray[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = toArray[index];
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
        int index;
        size = 0;
        threshhold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newArray = (Node<K,V>[]) new Node[newCapacity];
        for (Node<K, V> node : array) {
            while (node != null) {
                index = node.hash % newArray.length;
                put(newArray, new Node<>(node), index);
                node = node.next;
            }
        }
        array = newArray;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % array.length;
        Node<K, V> node = array[index];
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
