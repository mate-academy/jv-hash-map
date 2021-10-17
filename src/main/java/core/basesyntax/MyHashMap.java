package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private int size;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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
        table[getIndex(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
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

    private int getIndex(Object key) {
        return (key == null) ? 0 : (Math.abs((key.hashCode()))) % capacity;
    }

    private void resize() {
        size = 0;
        threshold *= 2;
        Node<K, V>[] oldArray = table;
        table = new Node[capacity * 2];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
