package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkForResize();
        int index = hash(key, table.length);
        Node<K,V> node = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K,V> newNode = new Node<>(key,value,table[index]);
        table[index] = newNode;
        size++;
        checkForResize();
    }

    @Override
    public V getValue(K key) {
        Node<K,V> value = table[hash(key,table.length)];
        while (value != null) {
            if (Objects.equals(value.key, key)) {
                return value.value;
            }
            value = value.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkForResize() {
        if (size > table.length * LOAD_FACTOR) {
            Node<K,V>[] oldTable = table;
            table = new Node[table.length << 1];
            size = 0;
            int i = 0;
            Node<K,V> value = oldTable[i];
            do {
                if (value == null) {
                    value = oldTable[i++];
                } else {
                    put(value.key, value.value);
                    value = value.next;
                }
            } while (oldTable.length > i);
        }
    }

    private int hash(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode()) % length;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
