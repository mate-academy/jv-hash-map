package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int RESIZE_VALUE = 2;
    private static final double LOAD_FACTOR = 0.75;
    private static final int BEGIN_LENGTH = 1 << 4;
    private Node<K, V>[] data;
    private int size;

    public MyHashMap() {
        data = new Node[BEGIN_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        if (size >= data.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> currentNode = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> currentBucket = data[index];
        if (currentBucket == null) {
            data[index] = currentNode;
            size++;
            return;
        }
        do {
            if (currentBucket.next == null) {
                currentBucket.next = currentNode;
                size++;
                return;
            }
            if (Objects.equals(currentBucket.next.key, key)) {
                currentBucket.value = value;
                return;
            }

            currentBucket.next = currentNode;
            size++;

            currentBucket = currentBucket.next;
        } while (currentBucket != null);

//        while (currentBucket.next != null) {
//            if (Objects.equals(currentBucket.key, key)) {
//                currentBucket.value = value;
//                return;
//            }
//            currentBucket = currentBucket.next;
//        }
//        if (Objects.equals(currentBucket.key, key)) {
//            currentBucket.value = value;
//            return;
//        }
//        currentBucket.next = currentNode;
//        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeInBucket = data[getIndex(key)];
        while (nodeInBucket != null) {
            if (Objects.equals(nodeInBucket.key, key)) {
                return nodeInBucket.value;
            }
            nodeInBucket = nodeInBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % data.length;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldData = data;
        data = new Node[oldData.length * RESIZE_VALUE];
        for (Node<K,V> buckedNode : oldData) {
            while (buckedNode != null) {
                put(buckedNode.key, buckedNode.value);
                buckedNode = buckedNode.next;
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
