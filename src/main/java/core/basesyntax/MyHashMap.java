package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexFromKey(key);
        if (table[index] != null) {
            Node<K, V> localCurrent = table[index];
            if (Objects.equals(localCurrent.key, key)) {
                localCurrent.value = value;
                return;
            }
            while (localCurrent.next != null) {
                localCurrent = localCurrent.next;
                if (Objects.equals(localCurrent.key, key)) {
                    localCurrent.value = value;
                    return;
                }
            }
            localCurrent.next = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        table[index] = new Node<K, V>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndexFromKey(key)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexFromKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size + 1 > table.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] temp = table;
            table = (Node<K, V>[]) new Node[table.length << 1];
            for (Node<K, V> kvNode : temp) {
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
