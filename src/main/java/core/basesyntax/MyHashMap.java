package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_INCREMENT = 2;

    private Node<K, V>[] data;
    private int size;
    private int threshold;

    public MyHashMap() {
        data = (Node<K, V>[])new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        private Node<K, V> next;
        private V value;
        private final K key;

        public Node(Node<K, V> next, V value, K key) {
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        final Node<K, V> newNode = new Node<>(null, value, key);
        int index = getPosition(key);
        Node<K, V> temp = data[index];
        if (temp == null) {
            data[index] = newNode;
            size++;
            return;
        }
        while (true) {
            if (Objects.equals(temp.key, key)) {
                temp.value = value;
                return;
            }
            if (temp.next == null) {
                temp.next = newNode;
                size++;
                return;
            }
            temp = temp.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchNodeByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = data.length * CAPACITY_INCREMENT;
        final Node<K, V>[] oldData = data;
        data = (Node<K, V>[])new Node[newCapacity];
        size = 0;
        threshold *= CAPACITY_INCREMENT;
        for (Node<K, V> element : oldData) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private Node<K, V> searchNodeByKey(K key) {
        int index = getPosition(key);
        Node<K, V> temp = data[index];
        while (temp != null) {
            if (Objects.equals(temp.key, key)) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    private int getPosition(K key) {
        return Math.abs((key == null ? 0 : key.hashCode()) % data.length);
    }
}
