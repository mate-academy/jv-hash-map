package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> current = table[getIndex(key)];
        if (current == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
        } else {
            boolean flag = true;
            while (flag) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    flag = false;
                } else {
                    current = current.next;
                }
            }
            current.next = new Node<>(key, value, null);
        }
        size++;
        if (size > getThreshold()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = getNode(key);
        return current == null ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newSize = table.length * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newSize];
        transfer(newTable);
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] tmp = table;
        table = newTable;
        for (Node<K, V> node : tmp) {
            while (node != null) {
                put(node.key, node.value);
                size--;
                node = node.next;
            }
        }
    }

    private Node<K,V> getNode(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode() < 0 ? key.hashCode() * (-1) : key.hashCode();
        return hash % table.length;
    }

    private int getThreshold() {
        return (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}


