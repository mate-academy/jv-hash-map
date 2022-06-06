package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75F;
    private static final int GROWTH_COEFFICIENT = 2;
    private static final int DEFAULT_SIZE = 16;
    private int size;
    private Node<K,V>[] values;

    public MyHashMap() {
        values = new Node[DEFAULT_SIZE];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 == values.length * LOAD_FACTOR) {
            resize();
        }
        Node<K,V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        setAtArray(index, newNode);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> bucket = values[index];
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            }
            bucket = bucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void setAtArray(int index, Node<K,V> newNode) {
        Node<K,V> bucket = values[index];
        if (bucket == null) {
            values[index] = newNode;
            size++;
        } else {
            setAtList(values[index], newNode);
        }
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldValues = values;
        values = new Node[oldValues.length * GROWTH_COEFFICIENT];
        for (Node<K,V> bucket : oldValues) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % values.length;
    }

    private void setAtList(Node<K,V> oldNode, Node<K,V> newNode) {
        boolean bool = false;
        while (!bool) {
            if (Objects.equals(oldNode.key, newNode.key)) {
                oldNode.value = newNode.value;
                return;
            } else if (oldNode.next == null) {
                oldNode.next = newNode;
                size++;
                return;
            }
            oldNode = oldNode.next;
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
