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
    private Node<K, V>[] table;
    private double threshold;

    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = new Node[DEFAULT_CAPACITY];
        threshold = capacity * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        putInTable(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> bucket = table[index];
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
        for (Node<K, V> bucket : table) {
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
        for (Node<K, V> bucket : table) {
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
        table = new Node[DEFAULT_CAPACITY];
        threshold = capacity * LOAD_FACTOR;
    }

    public V remove(K key) {
        int index = calculateIndex(key);
        Node<K, V> bucket = table[index];
        V value = getValue(key);
        if (Objects.equals(table[index].key, key)) {
            table[index] = table[index].next;
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
        for (Node<K, V> bucket : table) {
            while (bucket != null) {
                keySet.add(bucket.key);
                bucket = bucket.next;
            }
        }
        return keySet;
    }

    public Collection<V> values() {
        Collection<V> values = new HashSet<>();
        for (Node<K, V> bucket : table) {
            while (bucket != null) {
                values.add(bucket.value);
                bucket = bucket.next;
            }
        }
        return values;
    }

    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (Node<K, V> bucket : table) {
            while (bucket != null) {
                entrySet.add(new AbstractMap.SimpleEntry<>(bucket.key, bucket.value));
                bucket = bucket.next;
            }
        }
        return entrySet;
    }

    private void resize() {
        capacity *= CAPACITY_DELTA;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putInTable(node.key, node.value);
                node = node.next;
            }
        }
        threshold = capacity * LOAD_FACTOR;
    }

    private int calculateIndex(K key) {
        return key != null ? Math.abs(key.hashCode() % capacity) : 0;
    }

    private void putInTable(K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (current == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prev = current;
            while (current != null) {
                if (Objects.equals(newNode.key, current.key)) {
                    current.value = newNode.value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
