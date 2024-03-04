package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resizeTable();
        }
        int hash = getHash(key);
        int index = (key == null) ? 0 : getIndex(hash, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, hash);
            size++;
        } else {
            if (Objects.equals(table[index].key, key)) {
                table[index].value = value;
                return;
            }
            Node<K, V> current = table[index];
            while (current.next != null) {
                if ((key == null && current.key == null) || (key != null
                        && key.equals(current.key))) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current.next = new Node<>(key, value, hash);
            size++;
        }

    }

    public V getValue(K key) {
        int hash = getHash(key);
        int index = (key == null) ? 0 : getIndex(hash, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    private int getIndex(int hash, int tableLength) {
        return (hash & Integer.MAX_VALUE) % tableLength;
    }

    private void resizeTable() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int newIndex = (oldNode.key == null) ? 0 : getIndex(oldNode.hash, newCapacity);
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
        }
        table = newTable;
    }

    private int getHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = null;
        }
    }
}
