package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR  = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] buckets;
    private int size = 0;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    public MyHashMap(int capacity) {
        buckets = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (buckets[0] == null) {
                size++;
            }
            buckets[0] = new Node<K, V>(0, key, null, value);
            return;
        }
        if (size >= buckets.length * LOAD_FACTOR) {
            resize();
        }
        int index = getHash(key) % (buckets.length - 1);
        if (buckets[index] != null) {
            Node<K, V> tempNode = buckets[index];
            while (tempNode != null) {
                if (tempNode.key.equals(key)) {
                    buckets[index].value = value;
                    return;
                }
                tempNode = tempNode.next;
            }
            Node<K, V> localNode = buckets[index];
            buckets[index] = new Node<>(getHash(key), key, localNode, value);
        } else {
            buckets[index] = new Node<>(getHash(key), key, null, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return buckets[0].value;
        }
        int index = getHash(key) % (buckets.length - 1);
        Node<K, V> tempNode = buckets[getHash(key) % (buckets.length - 1)];
        while (tempNode != null) {
            if (Objects.equals(key,tempNode.key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(final K key) {
        return  31 * 17  + Math.abs(key.hashCode() >>> 20);
    }

    private static class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node next;

        private Node(int hash, K key, Node next, V value) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node[] newNode = new Node[(buckets.length << 1)];
        for (Node<K, V> localNode : buckets) {
            if (localNode != null) {
                int newIndex = localNode.hash % (newNode.length - 1);
                newNode[newIndex] = localNode;
            }

        }
        buckets = newNode;
    }
}
