package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return "key = " + key + ", value = " + value;
        }

    }

    @Override
    public void put(K key, V value) {
        int hash = getIndex(key);
        Node<K, V> temp = table[hash];
        Node<K, V> current = temp;
        if (temp == null) {
            table[hash] = new Node<K, V>(hash, key, value, null);
        } else {
            while (temp != null) {
                if (Objects.equals(temp.key, key)) {
                    temp.value = value;
                    return;
                }
                current = temp;
                temp = temp.next;
            }
            current.next = new Node<K, V>(hash, key, value, null);
        }
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        int hash = getIndex(key);
        Node<K, V> temp = table[hash];
        while (temp != null) {
            if (Objects.equals(temp.key, key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transferValue(K key, V value, Node<K, V>[] table) {
        int hash = getIndex(key);
        Node<K, V> temp = table[hash];
        if (temp == null) {
            table[hash] = new Node<K, V>(hash, key, value, null);
        } else {
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = new Node<K, V>(hash, key, value, null);
        }
    }

    private void resize() {
        if (table.length * DEFAULT_LOAD_FACTOR <= size) {
            Node<K, V>[] temp = table;
            table = new Node[table.length * 2];
            transfer(temp, table);
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private void transfer(Node<K, V>[] tableFrom, Node<K, V>[] tableTo) {
        Node<K, V> temp;
        for (int i = 0; i < tableFrom.length; i++) {
            temp = tableFrom[i];
            while (temp != null) {
                transferValue(temp.key, temp.value, tableTo);
                temp = temp.next;
            }
        }
    }
}
