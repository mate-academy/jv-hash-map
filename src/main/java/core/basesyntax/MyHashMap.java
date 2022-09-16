package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node<K, V> node = (Node<K, V>) o;
            return node.key.equals(key)
                    && node.value.equals(value);
        }
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void move(K key, V value, int hash, Node<K, V>[] newTable) {
        int tableIndex = getTableIndex(key, newTable.length);
        Node<K, V> element = newTable[tableIndex];
        if (element == null) {
            newTable[tableIndex] = new Node<>(hash, key, value, null);
        } else {
            while (element.next != null) {
                element = element.next;
            }
            element.next = new Node<>(hash, key, value, null);
        }
    }

    private Node<K, V>[] resize() {
        int oldCapacity = (table == null) ? 0 : table.length;
        threshold = threshold << 1;
        int newCapacity = oldCapacity << 1;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            if (table[i] != null) {
                Node<K, V> elem = table[i];
                while (elem != null) {
                    move(elem.key, elem.value, elem.hash, newTable);
                    elem = elem.next;
                }
            }
        }
        return newTable;
    }

    private int getTableIndex(K key, int tableLength) {
        return hashCode(key) % tableLength;
    }

    @Override
    public void put(K key, V value) {
        int hashCodeNode = hashCode(key);
        Node<K, V> lastElement;
        if (table == null) {
            table = (Node<K, V>[]) new Node[1 << 4];
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
            do {
                if (key == null && element.key == null
                        || element.key != null && element.key.equals(key)) {
                    element.value = value;
                    return;
                }
                lastElement = element;
                element = element.next;
            } while (element != null);
            lastElement.next = new Node<>(hashCodeNode, key, value, null);
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
        do {
            if (key == null && element.key == null ||
                    element.key != null && element.key.equals(key)) {
                return element.value;
            }
            element = element.next;
        } while (element != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
