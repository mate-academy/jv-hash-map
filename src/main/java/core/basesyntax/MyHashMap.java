package core.basesyntax;

import java.util.Objects;
/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private final double loadFactor = 0.75;
    private final int defaultDefinition = 16;
    private Node<K, V>[] bucket;
    private int threshold;

    public MyHashMap() {
        bucket = new Node[defaultDefinition];
        threshold = (int) (defaultDefinition * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = indexFor(key);
        Node<K, V> node = new Node<>(key, value);
        if (bucket[index] == null) {
            bucket[index] = node;
            size++;
            return;
        }
        Node<K, V> currentNode = bucket[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, node.key)) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[]tempBucket = bucket;
        bucket = (Node<K, V>[]) new Node[bucket.length * 2];
        threshold = (int) (bucket.length * loadFactor);
        for (Node<K, V> node : tempBucket) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        Node<K, V> currentNode = bucket[index];
        while (currentNode != null) {
            if ((currentNode.key == key)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
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

    public int indexFor(K key) {
        return key == null ? 0 : (key.hashCode()) & (bucket.length - 1);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
