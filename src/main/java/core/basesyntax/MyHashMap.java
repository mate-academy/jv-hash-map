package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[16];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = generateIndex(key);
        if (buckets[index] != null) {
            for (Node<K, V> currentNode = buckets[index];
                    currentNode != null; currentNode = currentNode.next) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(Objects.hash(key), key, value, null);
                    break;
                }
            }
        } else {
            buckets[index] = new Node<>(Objects.hash(key), key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = buckets[generateIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int generateIndex(K key) {
        return key == null ? 0 : key.hashCode() & 0xfffffff % buckets.length;
    }

    private void resize() {
        if (size >= buckets.length * 0.75) {
            int newCapacity = buckets.length * 2;
            Node<K, V>[] oldArray = buckets;
            buckets = new Node[newCapacity];
            transport(oldArray);
        }
    }

    private void transport(Node<K, V>[] oldBuckets) {
        size = 0;
        for (int i = 0; i < oldBuckets.length; i++) {
            Node<K, V> currentNode = oldBuckets[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

