package core.basesyntax;

import static java.util.Objects.hash;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V> next;
        int index = getBucketIndex(key);
        int hash = (key == null) ? 0 : hash(key);
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        }
        if ((table[index]) != null
                && (key != null && (!table[index].key.equals(key)))) {
            if ((next = table[index].next) != null) {
                if (table[index].next.next != null) {
                    while (next.next != null) {
                        next = next.next;
                    }
                }
                next.next = new Node<>(hash, key, value, null);
                size++;
            } else {
                table[index].next = new Node<>(hash, key, value, null);
                size++;
            }
        }
        if ((table[index]) != null && (table[index].key == key
                || (key != null && key.equals(table[index].key)))) {
            table[index].value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    final Node<K, V> getNode(Object key) {
        int index = getBucketIndex(key);
        int hash = (key == null) ? 0 : hash(key);
        if ((table) != null && (table.length) > 0
                && (table[index]) != null) {
            if (table[index].hash == hash && ((table[index].key) == key || key != null && key.equals(table[index].key))) {
                return table[index];
            }
            if ((table[index].next) != null) {
                do {
                    if (table[index].next.hash == hash
                            && ((table[index].next.key) == key || (key != null && key.equals(table[index].next.key)))) {
                        return table[index].next;
                    }
                    table[index].next = table[index].next.next;
                } while ((table[index].next) != null);
            }
        }
        return null;
    }

    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCapacity = (oldTab == null) ? 0 : oldTab.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold;
        if (oldCapacity > 0) {
            if ((getSize() > oldCapacity * DEFAULT_LOAD_FACTOR)) {
                newCapacity = oldCapacity << 1;
                newThreshold = oldThreshold << 1;
            } else {
                newCapacity = DEFAULT_INITIAL_CAPACITY;
                newThreshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            }
            threshold = newThreshold;
            Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCapacity];
            for (int i = 0; i < oldTab.length; i++) {
                Node<K, V> e;
                if ((e = oldTab[i]) != null) {
                    if (e.next != null) {
                        Node<K,V> loHead = null;
                        Node<K,V> loTail = null;
                        Node<K,V> hiHead = null;
                        Node<K,V> hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCapacity) == 0) {
                                if (loTail == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                if (hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[i + oldCapacity] = hiHead;
                        }
                    } else {
                        newTab[e.hash & (newCapacity - 1)] = e;
                    }
                }
            }
            table = newTab;
        }
        return table;
    }

    private int getBucketIndex(Object key) {
        int hash = (key == null) ? 0 : hash(key);
         return  (table.length - 1) & hash;
    }
}
