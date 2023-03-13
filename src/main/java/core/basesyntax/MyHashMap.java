package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int ARRAY_EXTEND_COEFFICIENT = 2;

    private int size;
    private Node[] values;

    public MyHashMap() {
        values = new Node[16];
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (values.length * LOAD_FACTOR)) {
            resize();
        }
        int elementPosition = findElementPosition(key);
        if (values[elementPosition] == null) {
            values[elementPosition] = new Node(key, value, null);
            size++;
            return;
        }
        Node node = values[elementPosition];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        int elementPosition = findElementPosition(key);
        Node node = values[elementPosition];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return (V) node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findElementPosition(K key) {
        int elementPosition;
        if (key == null) {
            elementPosition = 0;
        } else {
            elementPosition = Math.abs(key.hashCode() % values.length);
        }
        return elementPosition;
    }

    private void resize() {
        Node[] oldArray = Arrays.copyOf(values, values.length);
        values = new Node[values.length * ARRAY_EXTEND_COEFFICIENT];
        for (Node node : oldArray) {
            while (node != null) {
                int elementPosition = findElementPosition((K) node.key);
                if (values[elementPosition] == null) {
                    values[elementPosition] = new Node(node.key, node.value, null);
                } else {
                    Node tmpNode = values[elementPosition];
                    while (tmpNode.next != null) {
                        tmpNode = tmpNode.next;
                    }
                    tmpNode.next = new Node(node.key, node.value, null);
                }
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
