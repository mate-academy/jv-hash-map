package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> previous = table[getIndex(key)];
        if (previous == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
        } else {
            while (true) {
                if (Objects.equals(previous.key, key)) {
                    previous.value = value;
                    return;
                }
                if (previous.next == null) {
                    break;
                }
                previous = previous.next;
            }
            previous.next = new Node<>(key, value, null);
        }
        if (++size > getThreshold()) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
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

    private int getThreshold() {
        return (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private Node<K, V> getNode(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private int getIndex(K key) {
        int hash = key == null ? 0 : key.hashCode();
        return (table.length - 1) & hash;
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
