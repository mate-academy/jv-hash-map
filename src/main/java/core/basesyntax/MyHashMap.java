package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> node = new Node<>(hash(key), key, value, null);
        int index = node.hash % table.length;
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K,V> pointer = table[index];
            while (pointer != null) {
                if (Objects.equals(pointer.key, key)) {
                    pointer.value = value;
                    return;
                }
                if (pointer.next == null) {
                    pointer.next = node;
                    break;
                }
                pointer = pointer.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        Node<K,V>[] newTable;
        newTable = (Node<K,V>[]) new Node[oldTable.length * 2];
        threshold = (int)(newTable.length * DEFAULT_LOAD_FACTOR);
        transfer(oldTable, newTable);
        table = newTable;
    }

    private void transfer(Node<K,V>[] oldTable, Node<K,V>[] newTable) {
        for (Node<K,V> node : oldTable) {
            if (node == null) {
                continue;
            }
            int index = node.hash % newTable.length;
            if (newTable[index] != null) {
                Node<K, V> pointer = newTable[index];
                while (pointer.next != null) {
                    if (Objects.equals(pointer.key, node.key)) {
                        pointer.value = node.value;
                        break;
                    }
                    pointer = pointer.next;
                }
            }
            newTable[index] = node;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int index = hash(key) % table.length;
        Node<K,V> pointer = table[index];
        while (pointer != null) {
            if (Objects.equals(pointer.key,key)) {
                value = pointer.value;
            }
            pointer = pointer.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = hash(key) % table.length;
        Node<K,V> pointer = table[index];
        while (pointer.next != null) {
            if (Objects.equals(key, pointer.key)) {
                return true;
            }
            pointer = pointer.next;
        }
        return false;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() & 1 << 16;
    }
}
