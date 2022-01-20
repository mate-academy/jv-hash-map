package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int SIZE_INCREASE_INDEX = 2;
    private Node<K, V>[] map;
    private int threshold;
    private int size;

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

    public MyHashMap() {
        this.map = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (map[index] == null) {
            putNodeInEmptyBucket(key, value, index);
        } else {
            putNodeIfKeyExistsOrCollision(key, value, index);
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> newNode = map[index];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNodeInEmptyBucket(K key, V value, int index) {
        map[index] = new Node<>(key, value, null);
        size++;
    }

    private void putNodeIfKeyExistsOrCollision(K key, V value, int index) {
        Node<K, V> node = map[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                break;
            }
            node = node.next;
        }
        size++;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldMap = map;
        map = new Node[map.length * SIZE_INCREASE_INDEX];
        threshold = (int) (map.length * LOAD_FACTOR);
        for (Node<K, V> node: oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;

            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % map.length;
    }
}
