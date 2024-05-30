package core.basesyntax;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_DELTA = 2;
    private int capacity;
    private int size;
    private Node<K, V>[] buckets;
    private double threshold;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = capacity * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = key != null ? Math.abs(key.hashCode() % capacity) : 0;
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value);
            size++;
        } else if (Objects.equals(buckets[index].key, key)) {
            buckets[index].value = value;
        } else {
            Node<K, V> bucket = buckets[index];
            while (true) {
                if (bucket.next == null) {
                    bucket.next = new Node<>(key, value);
                    size++;
                    break;
                } else if (Objects.equals(bucket.next.key, key)) {
                    bucket.next.value = value;
                    break;
                } else {
                    bucket = bucket.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key != null ? Math.abs(key.hashCode() % capacity) : 0;
        Node<K, V> bucket = buckets[index];
        if (bucket == null) {
            return null;
        }

        if (Objects.equals(bucket.key, key)) {
            return bucket.value;
        } else {
            while (bucket != null) {
                if (Objects.equals(bucket.key, key)) {
                    return bucket.value;
                }
                bucket = bucket.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean containsKey(K key) {
        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                if (bucket.key.equals(key)) {
                    return true;
                }
                bucket = bucket.next;
            }
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                if (bucket.value.equals(value)) {
                    return true;
                }
                bucket = bucket.next;
            }
        }
        return false;
    }

    public void clear() {
        capacity = DEFAULT_CAPACITY;
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = capacity * LOAD_FACTOR;
    }

    public V remove(K key) {
        int index = key != null ? Math.abs(key.hashCode() % capacity) : 0;
        Node<K, V> bucket = buckets[index];
        V value = getValue(key);
        if (Objects.equals(buckets[index].key, key)) {
            buckets[index] = buckets[index].next;
            return value;
        }
        while (bucket != null) {
            if (Objects.equals(bucket.next.key, key)) {
                bucket.next = bucket.next.next;
                return value;
            } else {
                bucket = bucket.next;
            }
        }
        return null;
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                keySet.add(bucket.key);
                bucket = bucket.next;
            }
        }
        return keySet;
    }

    public Collection<V> values() {
        Collection<V> values = new HashSet<>();
        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                values.add(bucket.value);
                bucket = bucket.next;
            }
        }
        return values;
    }

    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (Node<K, V> bucket : buckets) {
            while (bucket != null) {
                entrySet.add(new AbstractMap.SimpleEntry<>(bucket.key, bucket.value));;
                bucket = bucket.next;
            }
        }
        return entrySet;
    }

    private void resize() {
        capacity *= CAPACITY_DELTA;
        Node<K, V>[] newBuckets = new Node[capacity];
        for (Node<K, V> bucket : buckets) {
            if (bucket == null) {
                continue;
            }

            int nodesNumber = 1;
            Node<K, V> countBucket = bucket;
            while (countBucket.next != null) {
                nodesNumber++;
                countBucket = countBucket.next;
            }

            Node<K, V>[] bucketNodes = new Node[nodesNumber];
            for (int i = 0; bucket != null; i++) {
                bucketNodes[i] = bucket;
                bucket = bucket.next;
                bucketNodes[i].next = null;
            }

            for (Node<K, V> bucketNode : bucketNodes) {
                int index = Math.abs(bucketNode.key != null ? bucketNode.key.hashCode() % capacity
                        : 0);
                bucket = newBuckets[index];
                while (true) {
                    if (bucket == null) {
                        newBuckets[index] = bucketNode;
                        break;
                    } else if (bucket.next == null) {
                        bucket.next = bucketNode;
                        break;
                    } else {
                        bucket = bucket.next;
                    }
                }
            }
        }
        buckets = newBuckets;
        threshold = capacity * LOAD_FACTOR;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
