package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndexFromKey(key);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = getNodeByKey(key);
            if (node != null) {
                node.value = value;
                return;
            } else {
                table[index] = new Node<>(key, value, table[index]);
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNodeByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexFromKey(Object key) {
        return (key == null) ? 0 : Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void resizeIfNeeded() {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private Node<K, V> getNodeByKey(K key) {
        Node<K, V> node = table[getIndexFromKey(key)];

        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
