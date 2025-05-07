package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int RESIZE_VALUE = 2;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] data;
    private int size;

    public MyHashMap() {
        data = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        if (size >= data.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> inputNode = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> thisBucket = data[index];
        if (thisBucket == null) {
            data[index] = inputNode;
            size++;
            return;
        }

        do {
            if (Objects.equals(thisBucket.key, key)) {
                thisBucket.value = value;
                return;
            }
            if (thisBucket.next == null) { //
                thisBucket.next = inputNode;
                size++;
                return;
            }
            thisBucket = thisBucket.next;
        } while (thisBucket != null);
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
}
