package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    public void put(K key, V value) {
        if (key == null) {
            resize();
            int index = 0;
            Node<K, V> node = buckets[index];
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            buckets[index] = new Node<>(key, value, buckets[index]);
            size++;
        } else {
            int index = getIndex(key);
            Node<K, V> newNode = new Node<>(key, value, null);
            if (buckets[index] == null) {
                buckets[index] = newNode;
                size++;
            } else {
                Node<K, V> currentNode = buckets[index];
                Node<K, V> prevNode = null;
                while (currentNode != null) {
                    if (Objects.equals(key, currentNode.key)) {
                        currentNode.value = value;
                        return;
                    }
                    prevNode = currentNode;
                    currentNode = currentNode.next;
                }
                if (prevNode != null) {
                    Node<K, V> newNextNode = prevNode.next;
                    prevNode.next = newNode;
                    newNode.next = newNextNode;
                    size++;
                }
            }
            if ((float) size / buckets.length >= LOAD_FACTOR) {
                resize();
            }
        }
    }

    public V getValue(K key) {
        for (Node<K, V> node : buckets) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int index = (key == null) ? 0 : (key.hashCode() % buckets.length);
        return index > 0 ? index : Math.abs(index);
    }

    private void resize() {
        if ((float) size / buckets.length >= LOAD_FACTOR) {
            int newCapacity = buckets.length * 2;
            Node<K, V>[] newBuckets = new Node[newCapacity];
            int newSize = 0;

            for (Node<K, V> node : buckets) {
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                    int newIndex = getIndex(node.key);
                    node.next = newBuckets[newIndex];
                    newBuckets[newIndex] = node;
                    node = nextNode;
                    newSize++;
                }
            }

            buckets = newBuckets;
            size = newSize;
        } else if (size < buckets.length / 2) {
            int newCapacity = buckets.length / 2;
            Node<K, V>[] newBuckets = new Node[newCapacity];
            int newSize = 0;

            for (Node<K, V> node : buckets) {
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                    int newIndex = getIndex(node.key);
                    node.next = newBuckets[newIndex];
                    newBuckets[newIndex] = node;
                    node = nextNode;
                    newSize++;
                }
            }

            buckets = newBuckets;
            size = newSize;
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
