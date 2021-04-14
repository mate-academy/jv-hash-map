package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] elements;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (elements == null || size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<K, V>(key, value, null);
        int index = getIndex(key);
        if (elements[index] == null) {
            elements[index] = newNode;
            size++;
            return;
        }
        Node<K, V> tempNode = elements[index];
        if (Objects.equals(key, tempNode.key)) {
            tempNode.value = value;
            return;
        }
        while (tempNode.next != null) {
            tempNode = tempNode.next;
            if (Objects.equals(key, tempNode.key)) {
                tempNode.value = value;
                return;
            }
        }
        tempNode.next = newNode;
        size++;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() % elements.length) > 0 ? key.hashCode() % elements.length
                : key.hashCode() % elements.length * (-1);
    }

    private void resize() {
        if (elements == null) {
            elements = new Node[INITIAL_CAPACITY];
            threshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
        } else {
            final Node<K, V>[] oldArray = elements;
            elements = new Node[elements.length * 2];
            threshold *= 2;
            size = 0;
            for (Node<K, V> element : oldArray) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (elements == null) {
            return null;
        }
        int index = getIndex(key);
        Node<K, V> tempNode = elements[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
