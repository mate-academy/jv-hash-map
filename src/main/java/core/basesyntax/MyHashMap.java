package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {

            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public static final int INITIAL_CAPACITY = 16;
    public static final int DEFAULT_MULTIPLIER = 2;
    public static final double LOAD_FACTOR = 0.75F;
    private Node<K, V>[] dataArray;
    private int size;
    private int threshold;

    public MyHashMap() {
        dataArray = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private int getIndex(K key) {
        if (key != null) {
            return Math.abs(key.hashCode() % dataArray.length);
        }
        return 0;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldDataArray = dataArray;
        dataArray = new Node[dataArray.length * DEFAULT_MULTIPLIER];
        threshold = (int) (dataArray.length * LOAD_FACTOR);
        for (Node<K, V> node : oldDataArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> currentNode = dataArray[getIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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
        dataArray[getIndex(key)] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : dataArray) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
