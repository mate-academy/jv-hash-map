package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private final int defaultInitialCapacity = 16;
    private final float defaultLoadFactor = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private final int maximumCapacity = 1 << 30;
    private int size;
    private int modCount = 0;
    private float loadFactor;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.loadFactor = defaultLoadFactor;
        this.table = new Node[defaultInitialCapacity];
        this.threshold = (int) (defaultInitialCapacity * loadFactor);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
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

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            return o instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }

    @Override
    public void put(K key, V value) {
        insertEntry(hash(key), key, value, false);
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

    private Node<K, V> getNode(Object key) {
        Node<K, V>[] tab;
        Node<K, V> first;
        Node<K, V> e;
        int n;
        int hash;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0
                && (first = tab[(n - 1) & (hash = hash(key))]) != null) {
            if (first.hash == hash && ((k = first.key) == key
                    || (key != null && key.equals(k)))) {
                return first;
            }
            while ((e = first.next) != null) {
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                    return e;
                }
                first = e;
            }
        }
        return null;
    }

    private V insertEntry(int hash, K key, V value, boolean onlyIfAbsent) {
        Node<K, V>[] tables;
        Node<K, V> temp;
        int lenghtTable;
        int index;

        if ((tables = table) == null || (lenghtTable = tables.length) == 0) {
            lenghtTable = (tables = resize()).length;
        }

        index = (lenghtTable - 1) & hash;
        temp = tables[index];

        if (temp == null) {
            tables[index] = newNode(hash, key, value, null);
            size++;
            modCount++;

            if (size > threshold) {
                resize();
            }
            return null;
        }

        Node<K, V> e = findNodeWithKey(temp, hash, key);
        if (e != null) {
            return updateValueIfNeeded(e, value, onlyIfAbsent);
        } else {
            addNodeToEnd(temp, hash, key, value);
            if (size > threshold) {
                resize();
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity > maximumCapacity) {
            newCapacity = maximumCapacity;
        }

        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);
        table = newTable;

        if (oldTable != null) {
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    Node<K, V> next = node.next;
                    int index = (newCapacity - 1) & node.hash;
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = next;
                }
            }
        }

        return newTable;
    }

    private void addNodeToEnd(Node<K, V> start, int hash, K key, V value) {
        Node<K, V> current = start;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode(hash, key, value, null);
        ++size;
        ++modCount;
    }

    private Node<K, V> findNodeWithKey(Node<K, V> start, int hash, K key) {
        Node<K, V> current = start;
        while (current != null) {
            if (current.hash == hash && keysEqual(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private V updateValueIfNeeded(Node<K, V> node, V newValue, boolean onlyIfAbsent) {
        V oldValue = node.value;
        if (!onlyIfAbsent || oldValue == null) {
            node.value = newValue;
        }
        return oldValue;
    }

    private Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private boolean keysEqual(K k1, K k2) {
        return Objects.equals(k1, k2);
    }
}
