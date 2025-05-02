package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int defaultCapacity = 16;
    private double defaultLoadFactor = 0.75;
    private int size;
    private LinkedList<Node<K, V>>[] buckets;

    public MyHashMap() {
        this.buckets = new LinkedList[defaultCapacity];
        this.size = 0;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void resize() {
        LinkedList<Node<K, V>>[] oldBucket = buckets;
        buckets = new LinkedList[oldBucket.length * 2];
        size = 0;
        for (LinkedList<Node<K, V>> bucket : oldBucket) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    put(node.key, node.value);
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % buckets.length;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }
        for (Node<K, V> node : buckets[index]) {
            if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
        }
        buckets[index].add(new Node<>(key,value));
        size++;
        if (size > buckets.length * defaultLoadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Node<K, V> node : buckets[index]) {
                if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
