package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexForKey(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (size >= threshold) {
            resize();
        }
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
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
            node.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexForKey(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                break;
            }
            node = node.next;
        }
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexForKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tempTable = table;
        table = new Node[table.length * GROW_FACTOR];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < tempTable.length; i++) {
            Node<K, V> node = tempTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
