package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] buckets;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> lastNode = buckets[index];
            while (lastNode.next != null || Objects.equals(lastNode.key, newNode.key)) {
                if (Objects.equals(lastNode.key, newNode.key)) {
                    lastNode.value = newNode.value;
                    return;
                }
                lastNode = lastNode.next;
            }
            lastNode.next = newNode;
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> checkElement = buckets[index];
        while (checkElement != null) {
            if (Objects.equals(checkElement.key, key)) {
                return checkElement.value;
            }
            checkElement = checkElement.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % buckets.length);
    }

    private void resize() {
        size = 0;
        threshold *= MULTIPLIER;
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[buckets.length * MULTIPLIER];
        transfer(oldBuckets);
    }

    private void transfer(Node<K, V>[] oldBuckets) {
        for (Node<K, V> element : oldBuckets) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }
}
