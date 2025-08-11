package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int newCapacity = INITIAL_CAPACITY;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (buckets.length * LOAD_FACTOR);
        if (size > threshold) {
            resize();
        }
        int index = calculateIndex(key);
        Node<K, V> head = buckets[index];
        // If there is already a chain at this index
        if (head != null) {
            Node<K, V> current = head;
            while (true) {
                // If such a key already exists, simply update the value
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                }
                // If this is the last node in the list, we add a new one to the end
                if (current.next == null) {
                    current.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
        // If the chain is empty, simply insert a new node
        buckets[index] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = buckets[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        newCapacity = newCapacity * 2;
        Node<K, V>[] oldBuckets = buckets;
        buckets = (Node<K, V>[]) new Node[newCapacity];
        for (Node<K, V> head : oldBuckets) {
            Node<K, V> current = head;
            while (current != null) {
                put(current.key, current.value); // пересчитываем индекс
                current = current.next;
            }
        }
    }

    private int calculateIndex(K key) {
        int hash = hashCode(key);
        return (hash & 0x7fffffff) % buckets.length;
    }

    private int hashCode(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
