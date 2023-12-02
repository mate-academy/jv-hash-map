package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_FACTOR = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current;
        current = getNode(key);
        return (current == null) ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int hash, K key, V value) {
        Node<K, V>[] data;
        Node<K, V> parent;
        int n;
        if ((data = table) == null || (n = table.length) == 0) {
            data = resize();
            n = data.length;
        }
        int index = (n - 1) & hash;
        if ((parent = data[index]) == null) {
            data[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> current;
            K k;
            if (parent.hash == hash && ((k = parent.key) == key
                    || (key != null && key.equals(k)))) {
                current = parent;
            } else {
                current = parent.next;
                while (current != null) {
                    if (current.hash == hash
                            && ((k = current.key) == key || (key != null && key.equals(k)))) {
                        break;
                    }
                    parent = current;
                    current = current.next;
                }
                if (current == null) {
                    parent.next = new Node<>(hash, key, value, null);
                    size++;
                }
            }
            if (current != null) {
                current.value = value;
            }
        }
        if (size > threshold) {
            resize();
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (key.hashCode() % INITIAL_CAPACITY);
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold;
        if (oldCapacity > 0) {
            newCapacity = oldCapacity * GROWTH_FACTOR;
            newThreshold = oldThreshold * GROWTH_FACTOR;
        } else {
            newCapacity = INITIAL_CAPACITY;
            newThreshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
        }
        threshold = newThreshold;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        table = newTable;
        transfer(oldTable, newTable, oldCapacity, newCapacity);
        return table;
    }

    private void transfer(Node<K, V>[] oldTable, Node<K, V>[] newTable,
                          int oldCapacity, int newCapacity) {
        if (oldTable != null) {
            for (int i = 0; i < oldCapacity; i++) {
                Node<K, V> current = oldTable[i];
                if (current != null) {
                    oldTable[i] = null;
                    if (current.next == null) {
                        newTable[current.hash & (newCapacity - 1)] = current;
                    } else {
                        Node<K, V> next;
                        do {
                            next = current.next;
                            int newIndex = current.hash & (newCapacity - 1);
                            current.next = newTable[newIndex];
                            newTable[newIndex] = current;
                        } while ((current = next) != null);
                    }
                }
            }
        }
    }

    private Node<K, V> getNode(Object key) {
        if (table != null) {
            int hash = hash(key);
            Node<K, V> first = table[(table.length - 1) & hash];
            while (first != null) {
                if (first.hash == hash && Objects.equals(first.key, key)) {
                    return first;
                }
                first = first.next;
            }
        }
        return null;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final int hash;
        private final K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
