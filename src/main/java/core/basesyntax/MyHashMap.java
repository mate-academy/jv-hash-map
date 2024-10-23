package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int defaultCapacity = 16;
    public static final double loadFactor = 0.75;
    public static final int multiplier = 2;
    private int size;
    private Node<K,V>[] table = new Node[defaultCapacity];

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * loadFactor)) {
            resize();
        }
        int bucket = getBucket(key);
        Node<K,V> lastNode = table[bucket];
        if (lastNode == null) {
            table[bucket] = new Node<>(key, value, null);
            size++;
        } else {
            while (lastNode != null) {
                if (Objects.equals(lastNode.key, key)) {
                    lastNode.value = value;
                    return;
                } else if (lastNode.next == null) {
                    lastNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                } else {
                    lastNode = lastNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key);
        Node<K,V> node = table[bucket];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * multiplier];
        for (Node<K, V> old : oldTable) {
            while (old != null) {
                this.put(old.key, old.value);
                old = old.next;
            }
        }
    }

    public int getBucket(K key) {
        return Math.abs(getHash(key) % table.length);
    }

    public int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    static class Node<K,V> {
        private Node<K,V> next;
        final private K key;
        private V value;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
