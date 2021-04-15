package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLAYER = 2;

    private Node<K, V>[] buckets;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
        int index = Math.abs(newNode.hash % buckets.length);
        if (buckets[index] == null) {
            buckets[index] = newNode;
        } else {
            Node<K, V> lastElement = buckets[index];
            while (lastElement.next != null || newNode.key == lastElement.key
                    || Objects.equals(lastElement.key, newNode.key)) {
                if (Objects.equals(lastElement.key, newNode.key)) {
                    lastElement.value = newNode.value;
                    return;
                }
                lastElement = lastElement.next;
            }
            lastElement.next = newNode;
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(getHash(key) % buckets.length);
        Node<K, V> checkElement = buckets[index];
        if (buckets[index] != null) {
            while (checkElement.next != null) {
                if (Objects.equals(checkElement.key, key)) {
                    return checkElement.value;
                }
                checkElement = checkElement.next;
            }
            return checkElement.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        final private int hash;
        final private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        size = 0;
        threshold *= MULTIPLAYER;
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[buckets.length * MULTIPLAYER];
        transfer(oldBuckets);
    }

    private void transfer(Node<K, V>[] oldBuckets) {
        for (Node<K, V> element : oldBuckets) {
            if (element == null) {
                continue;
            }
            while (element.next != null) {
                put(element.key, element.value);
                element = element.next;
            }
            put(element.key, element.value);
        }
    }
}
