package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(e.key, key)) {
                e.value = value;
                return;
            }
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (key == e.key || (key != null && key.equals(e.key)))) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void putNullKey(V value) {
        for (Node<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                e.value = value;
                return;
            }
        }
        addNode(0, null, value, 0);
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        if (++size > (table.length * LOAD_FACTOR)) {
            resize(table.length * 2);
        }
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] src = table;
        for (int j = 0; j < src.length; j++) {
            Node<K, V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Node<K, V> next = e.next;
                    int i = indexFor(e.hash, newTable.length);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

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
    }
}
