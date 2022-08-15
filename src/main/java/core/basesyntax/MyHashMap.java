package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshole;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshole = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> currNode = buckets[index];
            while (currNode.next != null) {
                currNode = currNode.next;
            }
            currNode.next = new Node<>(key, value, null);
        }
        if (++size == threshole) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] == null) {
            return null;
        } else {
            Node<K, V> currNode = buckets[index];
            while (currNode.next != null) {
                if(Objects.equals(currNode.key,key)) {
                    return currNode.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {

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
}
