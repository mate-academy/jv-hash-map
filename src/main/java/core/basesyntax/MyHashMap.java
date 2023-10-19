package core.basesyntax;

import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size = 0;
    private LinkedList<Node<K, V>>[] buckets = new LinkedList[16];

    private int hashCode(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % 16;
    }

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        if (buckets[hash] == null) {
            buckets[hash] = new LinkedList<>();
        }
        for (Node<K, V> node : buckets[hash]) {
            if ((key == null && node.getKey() == null)
                    || (key != null && key.equals(node.getKey()))) {
                node.setValue(value);
                return;
            }
        }
        buckets[hash].add(new Node<K, V>(hash, key, value, null));
        size++;

        if ((double) size / buckets.length >= 0.75) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hashCode(key);
        LinkedList<Node<K, V>> bucket = buckets[hash];
        if (bucket != null) {
            for (Node<K, V> node : bucket) {
                if ((key == null && node.getKey() == null)
                        || (key != null && key.equals(node.getKey()))) {
                    return node.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int capacity = buckets.length * 2;
        LinkedList<Node<K, V>>[] resizeBuckets = new LinkedList[capacity];

        for (LinkedList<Node<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Node<K, V> node : bucket) {
                    int newHashCode = hashCode(node.getKey()) % capacity;
                    if (resizeBuckets[newHashCode] == null) {
                        resizeBuckets[newHashCode] = new LinkedList<>();
                    }
                    resizeBuckets[newHashCode].add(node);
                }
            }
        }
        buckets = resizeBuckets;
    }
}
