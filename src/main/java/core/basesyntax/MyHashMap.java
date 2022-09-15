package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int threshold;
    private Node<K, V>[] table;
    private int size;

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
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

        public final int getHash() {
            return hash;
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

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node<K, V> node = (Node<K, V>) o;
            return node.getKey().equals(key)
                    && node.getValue().equals(value);
        }
    }

    private int hashCode(K key) {
        return Math.abs(Objects.hash(key));
    }

    private void move(K key, V value, Node<K, V>[] newTable) {
        int hashCodeNode = hashCode(key);

        int tableIndex = getTableIndex(key, newTable.length);

        Node<K, V> element = newTable[tableIndex];
        if (element == null) {
            newTable[tableIndex] = new Node<>(hashCodeNode, key, value, null);
        } else {
            while (key != null && element.getKey() != null
                    && element.next != null && !element.getKey().equals(key)) {
                element = element.next;
            }
            if (key != null && !element.getKey().equals(key)) {
                element.next = new Node<>(hashCodeNode, key, value, null);
            } else {
                element.setValue(value);
            }
        }
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        threshold = threshold << 1;
        int newCapacity = oldCapacity << 1;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            if (table[i] != null) {
                Node<K, V> elem = table[i];
                while (elem != null) {
                    move(elem.getKey(), elem.getValue(), newTable);
                    elem = elem.next;
                }
            }
        }
        return newTable;
    }

    private int getTableIndex(K key, int tableLength) {
        if (key == null) {
            return 0;
        }
        return hashCode(key) % tableLength;
    }

    @Override
    public void put(K key, V value) {
        int hashCodeNode = hashCode(key);
        if (table == null) {
            table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            table[getTableIndex(key, table.length)] = new Node<>(hashCodeNode, key, value, null);
            size = 1;
            return;
        }
        int tableIndex = getTableIndex(key, table.length);
        Node<K, V> element = table[tableIndex];
        if (element == null) {
            table[tableIndex] = new Node<>(hashCodeNode, key, value, null);
        } else {
            while (key != null && element.key != null
                    && element.next != null && !element.getKey().equals(key)) {
                element = element.next;
            }
            if (key != null && !element.getKey().equals(key)) {
                element.next = new Node<>(hashCodeNode, key, value, null);
            } else {
                element.setValue(value);
                return;
            }
        }
        if (++size > threshold) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int tableIndex = getTableIndex(key, table.length);
        if (table[tableIndex] == null) {
            return null;
        }
        Node<K, V> element = table[tableIndex];
        while (key != null && element.key != null
                && element.next != null && !element.getKey().equals(key)) {
            element = element.next;
        }
        if ((key == null && element.key == null) || (element.getKey().equals(key))) {
            return element.getValue();
        } else {
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }
}
