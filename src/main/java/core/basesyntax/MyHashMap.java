package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private Node<K,V>[] table;
    private int threshold;
    private int size;
    private final float loadFactor = 0;

    private static class Node<K,V> implements Map.Entry<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = resize();
        }
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }

        int index = hash & (table.length - 1);

        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if ((e.hash == hash) && ((e.key == null && key == null)
                    || (e.key != null && e.key.equals(key)))) {
                e.value = value;
                return;
            }
        }

        addNode(hash, key, value, index);
    }

    void addNode(int hash, K key, V value, int bucketIndex) {
        Node<K, V> e = table[bucketIndex];

        table[bucketIndex] = new Node<>(hash, key, value, e);

        size++;

        if (size > threshold) {
            resize();
        }
    }

    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash;
        if (key == null) {
            hash = 0;
        } else {
            hash = key.hashCode();
        }

        int index = hash & (table.length - 1);

        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if ((e.hash == hash) && (Objects.equals(e.key, key))) {
                return e.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap;
        int newThr = 0;

        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY
                    && oldCap >= DEFAULT_CAPACITY) {
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = DEFAULT_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
        }

        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY
                    ? (int)ft : Integer.MAX_VALUE);
        }

        threshold = newThr;
        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;

        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                while ((e = oldTab[j]) != null) {
                    oldTab[j] = e.next;
                    int hash = e.hash;

                    int indexInNewTable = hash & (newCap - 1);

                    e.next = newTab[indexInNewTable];
                    newTab[indexInNewTable] = e;
                }
            }
        }

        return newTab;
    }
}
