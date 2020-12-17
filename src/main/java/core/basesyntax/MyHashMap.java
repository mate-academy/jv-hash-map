package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return "key = " + key + ", value = " + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
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
        int hash = hash(key);
        Node<K, V> temp = table[hash];
        if (temp != null) {
            while (temp != null) {
                if (Objects.equals(temp.key, key)) {
                    return temp.value;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transferValue(K key, V value, Node<K, V>[] table) {
        int hash = hash(key);
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
            table = transfer(temp);
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private Node<K, V>[] transfer(Node<K, V>[] table) {
        Node<K, V>[] transferedTable = new Node[table.length * 2];
        Node<K, V> temp;
        for (int i = 0; i < table.length; i++) {
            temp = table[i];
            while (temp != null) {
                transferValue(temp.key, temp.value, transferedTable);
                temp = temp.next;
            }
        }
        return transferedTable;
    }
}
