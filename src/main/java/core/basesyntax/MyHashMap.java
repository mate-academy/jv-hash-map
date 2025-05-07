package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int RESIZE_CONSTANT = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] arrayNode;

    public MyHashMap() {
        arrayNode = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        resize();
        putElement(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = arrayNode[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % arrayNode.length;
    }

    private void putElement(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);

        if (arrayNode[index] == null) {
            arrayNode[index] = newNode;
            size++;
            return;
        }

        Node<K, V> current = arrayNode[index];
        while (current.next != null && !Objects.equals(current.key, key)) {
            current = current.next;
        }
        if (Objects.equals(current.key, key)) {
            current.value = value;
        } else {
            current.next = newNode;
            size++;
        }
    }

    private void resize() {
        int threshold = (int) (arrayNode.length * LOAD_FACTOR);
        if (size == threshold) {
            int newCapacity = arrayNode.length * RESIZE_CONSTANT;
            Node<K, V>[] tempArrayNode = arrayNode;
            arrayNode = new Node[newCapacity];
            size = 0;
            for (Node<K, V> node : tempArrayNode) {
                while (node != null) {
                    putElement(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
