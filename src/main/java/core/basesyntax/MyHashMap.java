package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int threshold = DEFAULT_SIZE;
    private int hashMapSize;

    @Override
    public void put(K key, V value) {
        if (table == null || hashMapSize >= threshold) {
            resize();
        }

        int hash = key == null ? 0 : key.hashCode();
        int index = (table.length - 1) & hash;

        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        table[index] = new Node<>(table[index], hash, key, value);
        hashMapSize++;
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        int hash = key == null ? 0 : key.hashCode();
        int index = (table.length - 1) & hash;

        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        if (++hashMapSize > threshold) {
            resize();
        }
        return null;
    }

    @Override
    public int getSize() {
        return hashMapSize;
    }

    public Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold = 0;
        if (oldCapacity > 0) {
            if (oldCapacity >= MAX_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTable;
            } else if ((newCapacity = oldCapacity << 1) < MAX_CAPACITY
                    && oldCapacity >= DEFAULT_SIZE) {
                newThreshold = oldThreshold << 1;
            }
        } else if (oldThreshold > 0) {
            newCapacity = oldThreshold;
        } else {
            newCapacity = DEFAULT_SIZE;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_SIZE);
        }
        if (newThreshold == 0) {
            float ft = (float) newCapacity * DEFAULT_LOAD_FACTOR;
            newThreshold = (newCapacity < MAX_CAPACITY && ft < (float) MAX_CAPACITY
                    ? (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThreshold;

        //noinspection unchecked
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        table = newTable;
        if (oldTable != null) {
            for (int i = 0; i < oldCapacity; ++i) {
                Node<K, V> e;
                if ((e = oldTable[i]) != null) {
                    oldTable[i] = null;
                    if (e.next == null) {
                        newTable[e.hash & (newCapacity - 1)] = e;
                    } else {
                        Node<K, V> loHead = null;
                        Node<K, V> loTail = null;
                        Node<K, V> hiTail = null;
                        Node<K, V> hiHead = null;
                        Node<K, V> next;
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
                            newTable[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTable[i + oldCapacity] = hiHead;
                        }
                    }
                }
            }
        }
        return newTable;
    }

    static class Node<K, V> {
        private Node<K, V> next;
        private final int hash;
        private final K key;
        private V value;

        public Node(Node<K, V> next, int hash, K key, V value) {
            this.next = next;
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(next, node.next)
                    && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(next, hash, getKey(), getValue());
        }
    }
}
