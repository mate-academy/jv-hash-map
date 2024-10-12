package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private Node<K, V> []table = new Node[capacity];
    private int size = 0;
    private int threhold = 0;

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        node.hash = node.hashCode();
        if (size++ > threhold) {
            resize();
        } else {
            for (int i = 0; i < table.length; i++) {
                if (node.value == null) {
                    table[0] = node;
                    size++;
                }
                if (table[i].hash == node.hash) {
                    size++;
                    table[i].next = node;
                } else {
                    table[node.hash] = node;
                    node.next = table[node.hash + 1];
                    size++;
                }
            }
        }

    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        Node<K, V> []table = new Node[capacity];
        capacity = capacity * 2;
        Node<K, V> []newTable = new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            table[i].hash = table[i].hash % capacity;
            newTable[table[i].hash] = table[i];
            newTable[table[i].hash].next = newTable[table[i].hash + 1];
        }
        System.arraycopy(newTable, 0, table, 0, newTable.length);
    }

    public int getIndex(K key) {
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i].key == key || (table[i] != null && table[i].key.equals(key))) {
                index = i;
            }
        }
        return index;
    }

    class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public int getHash() {
            return hash;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
