package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> checkNode = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (checkNode == null) {
            size++;
            table[index] = newNode;
            return;
        }
        while (newNode != null) {
            if (Objects.equals(checkNode.key, key)) {
                checkNode.value = value;
                return;
            }
            if (checkNode.next == null) {
                checkNode.next = newNode;
                size++;
                return;
            }
            checkNode = checkNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
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
        if (size > DEFAULT_LOAD_FACTOR * table.length) {
            Node<K, V>[] newTable = table;
            table = new Node[table.length << 1];
            size = 0;
            for (Node<K, V> node : newTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
