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

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> nodeToPut = new Node<K, V>(key, value, null);
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = nodeToPut;
            ++size;
            return;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    break;
                }
                node = node.next;
            }
            node.next = nodeToPut;
        }
        ++size;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private void resize() {
        size = 0;
        int capacity = table.length;
        Node<K, V>[] tempTable = table;
        capacity = capacity << 1;
        table = new Node[capacity];
        for (Node<K, V> element : tempTable) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
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
