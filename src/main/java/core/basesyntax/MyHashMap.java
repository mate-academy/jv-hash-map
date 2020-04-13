package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_CAPACITY = 16;
    private int size;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        final int index = determineIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new Node(key, value);
            size++;
            return;
        }
        Node<K, V> node = buckets[index];
        do {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                break;
            } else {
                node = node.next;
            }
        } while (true);
        node.next = new Node(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = buckets[determineIndex(key)];
        if (node != null) {
            do {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int determineIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        if (size >= buckets.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            final Node[] copyBuckets = buckets;
            buckets = new Node[copyBuckets.length * 2];
            for (int i = 0; i < copyBuckets.length; i++) {
                if (copyBuckets[i] != null) {
                    Node<K, V> node = copyBuckets[i];
                    while (node.next != null) {
                        put(node.next.key, node.next.value);
                        node = node.next;
                    }
                    put((K) copyBuckets[i].key, (V) copyBuckets[i].value);
                }
            }
        }
    }

    private static class Node<K, V> {
        Node<K, V> next;
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
