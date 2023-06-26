package core.basesyntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private List<LinkedList<Node<K,V>>> buckets;
    private int size;
    private Object[] arrayField;

    public MyHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        buckets = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            buckets.add(new LinkedList<>());
        }
        arrayField = new Object[0];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        LinkedList<Node<K, V>> list = buckets.get(index);

        for (Node<K, V> node : list) {
            if (node.key == null && key == null || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        list.add(new Node<>(key, value));
        size++;
        if (needsResize()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        LinkedList<Node<K, V>> list = buckets.get(index);
        for (Node<K, V> node : list) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.size());
    }

    private boolean needsResize() {
        float loadFactor = (float) size / buckets.size();
        return loadFactor > DEFAULT_LOAD_FACTOR;
    }

    private void resize() {
        List<LinkedList<Node<K, V>>> oldBuckets = buckets;
        int newCapacity = buckets.size() * 2;
        buckets = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            buckets.add(new LinkedList<>());
        }
        for (LinkedList<Node<K, V>> list : oldBuckets) {
            for (Node<K, V> node : list) {
                int index = getIndex(node.key);
                buckets.get(index).add(node);
            }
        }
        arrayField = new Object[newCapacity];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
