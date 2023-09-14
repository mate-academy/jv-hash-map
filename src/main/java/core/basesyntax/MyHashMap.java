package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index;
        if (table == null || table.length == 0 || size > threshold) {
            table = resize();
        }
        if (table[index = hash % table.length] == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> next = table[index];
            while (next != null) {
                if (Objects.equals(key, next.key)) {
                    next.value = value;
                    return;
                }
                if (next.next == null) {
                    next.next = new Node<>(hash, key, value, null);
                    break;
                }
                next = next.next;
            }
        }
        ++size;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K,V> first;
        if (table != null && table.length > 0
                && (first = table[hash % table.length]) != null) {
            do {
                if (Objects.equals(key, first.key)) {
                    return first.value;
                }
            } while ((first = first.next) != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        int oldCap = (table == null) ? 0 : table.length;
        int newCap;
        if (oldCap > 0) {
            threshold = threshold << 1;
            newCap = oldCap << 1;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (newCap * DEFAULT_LOAD_FACTOR);
            @SuppressWarnings({"rawtypes","unchecked"})
            Node<K, V>[] table = (Node<K, V>[]) new Node[newCap];
            return table;
        }
        Node<K, V>[] oldTab = table;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K, V>[] table = (Node<K, V>[]) new Node[newCap];
        for (int i = 0; i < oldCap; i++) {
            Node<K, V> element;
            if ((element = oldTab[i]) != null) {
                Node<K, V> first = element;
                do {
                    table[element.hash % table.length] = first;
                } while ((element = element.next) != null);
            }
        }
        return table;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
