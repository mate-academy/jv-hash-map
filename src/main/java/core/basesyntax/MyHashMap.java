package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int RESIZE_CONSTANT = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] arrayNode;

    public MyHashMap() {
        arrayNode = new Node[DEFAULT_SIZE];
        threshold = (int) (DEFAULT_SIZE * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        System.out.println(arrayNode.length + " " + key);
        Node<K, V> newNode = new Node<>(key, value, null);
        putElement(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : getIndex(key);
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
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = (key == null) ? 0 : getIndex(key);

        if (arrayNode[index] == null) {
            arrayNode[index] = newNode;
            size++;
        } else {
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
    }

    private void resize() {
        int newCapacity = arrayNode.length * RESIZE_CONSTANT;
        Node<K, V>[] tempArrayNode = arrayNode;
        arrayNode = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        for (int i = 0; i < tempArrayNode.length; i++) {
            Node<K, V> node = tempArrayNode[i];
            while (node != null) {
                Node<K, V> next = node.next;
                int index = (node.key == null) ? 0 : getIndex(node.key);
                node.next = arrayNode[index];
                arrayNode[index] = node;
                node = next;
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node<?, ?> node = (Node<?, ?>) o;

            if (!Objects.equals(key, node.key)) {
                return false;
            }
            if (!Objects.equals(value, node.value)) {
                return false;
            }
            return Objects.equals(next, node.next);
        }
    }
}
