package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final double LOAD_HASH = 0.75;

    private Node<K, V>[] headNode;
    private double threshold;
    private int size;

    public MyHashMap() {
        headNode = new Node[CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == headNode.length * LOAD_HASH) {
            resize();
        }
        int index = receiveHashCode(key, headNode.length);
        Node<K,V> newNode = findNode(key, index);
        if (newNode != null) {
            newNode.value = value;
        } else {
            Node<K,V> bucket = new Node(key,value,headNode[index]);
            headNode[index] = bucket;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = receiveHashCode(key, headNode.length);
        Node<K,V> bucket = findNode(key, index);
        return bucket != null ? bucket.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.nextNode = next;
        }
    }

    private int receiveHashCode(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode() % length);
    }

    private Node findNode(K key, int index) {
        Node<K,V> newNod = headNode[index];
        while (newNod != null) {
            if (Objects.equals(key, newNod.key)) {
                return newNod;
            }
            newNod = newNod.nextNode;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newNode = new Node[headNode.length * 2];
        size = 0;
        Node<K, V>[] oldBuckets = headNode;
        headNode = newNode;
        for (Node<K, V> bucket : oldBuckets) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.nextNode;
            }
        }
    }
}
