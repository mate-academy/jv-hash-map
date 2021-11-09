package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private int capacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (size == (capacity * LOAD_FACTOR)) {
            resize();
        }
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> nodeByIndex = table[index];
            while (nodeByIndex != null) {
                if (Objects.equals(nodeByIndex.key, key)) {
                    nodeByIndex.value = value;
                    return;
                }
                if (nodeByIndex.next == null) {
                    nodeByIndex.next = newNode;
                    size++;
                    return;
                }
                nodeByIndex = nodeByIndex.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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
        capacity = capacity * RESIZE_FACTOR;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        int index = key != null ? Math.abs(key.hashCode() % capacity) : 0;
        return index;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
