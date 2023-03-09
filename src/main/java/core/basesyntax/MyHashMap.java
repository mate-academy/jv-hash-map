package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int ARRAY_EXTEND_COEFFICIENT = 2;

    private int arrayCapacity = DEFAULT_CAPACITY;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private int size;
    private Node[] hashMapArray = new Node[arrayCapacity];

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int elementPosition = findElementPosition(key);
        if (hashMapArray[elementPosition] == null) {
            hashMapArray[elementPosition] = new Node(key, value, null);
            size++;
            return;
        }
        Node node = hashMapArray[elementPosition];
        while (true) {
            if (key == null || node.key == null) {
                if (key == null && node.key == null) {
                    node.value = value;
                    return;
                }
            } else if (node.key.equals(key)) {
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
        Node node = hashMapArray[elementPosition];
        while (node != null) {
            if (key == null || node.key == null) {
                if (key == null && node.key == null) {
                    return (V) node.value;
                }
            } else {
                if (node.key.equals(key)) {
                    return (V) node.value;
                }
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
            elementPosition = Math.abs(key.hashCode() % arrayCapacity);
        }
        return elementPosition;
    }

    private void resize() {
        arrayCapacity *= ARRAY_EXTEND_COEFFICIENT;
        threshold = (int) (arrayCapacity * LOAD_FACTOR);
        Node[] oldArray = Arrays.copyOf(hashMapArray, hashMapArray.length);
        hashMapArray = new Node[arrayCapacity];
        for (Node node : oldArray) {
            while (node != null) {
                int elementPosition = findElementPosition((K) node.key);
                if (hashMapArray[elementPosition] == null) {
                    hashMapArray[elementPosition] = new Node(node.key, node.value, null);
                } else {
                    Node tmpNode = hashMapArray[elementPosition];
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
