package core.basesyntax;

import java.util.LinkedList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_CAPACITY = 0.75f;
    private List<Node<K, V>>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new List[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            Node<K, V> lastNode = null;
            for (Node<K, V> node : buckets[index]) {
                lastNode = node;
                if (node.key == null && key == null) {
                    node.value = value;
                    return;
                } else if (node.key == null || key == null) {
                    continue;
                } else if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
            Node<K, V> newNode = createNewNode(key, value);
            buckets[index].add(newNode);
            lastNode.next = newNode;
        }
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
            buckets[index].add(createNewNode(key, value));
        }
        size++;
        resizeIfNeeded();
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (buckets[index] != null) {
            for (Node<K, V> node : buckets[index]) {
                if (key == null && node.key == null) {
                    return node.value;
                }
                if (node.key == null) {
                    continue;
                }
                if (node.key.equals(key)) {
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

    private void resizeIfNeeded() {
        if ((float) size / buckets.length > LOAD_CAPACITY) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = buckets.length << 1;
        List<Node<K, V>>[] newBuckets = new List[newCapacity];
        for (List<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newIndex = getHashCode(node.key) % newCapacity;
                    if (newBuckets[newIndex] == null) {
                        newBuckets[newIndex] = new LinkedList<>();
                    }
                    newBuckets[newIndex].add(node);
                }
            }
        }
        buckets = newBuckets;
    }

    private Node<K, V> createNewNode(K key, V value) {
        Node<K, V> node = new Node<K, V>(key, value);
        node.hash = getHashCode(key);
        return node;
    }

    private int getIndex(K key) {
        return getHashCode(key) % buckets.length;
    }

    private int getHashCode(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private int hash;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
