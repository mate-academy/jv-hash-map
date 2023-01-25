package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_GROW_VALUE = 2;
    private int size;
    private int threshold;

    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (threshold == size){
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (node == null) {
            table[index] = newNode;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                break;
            }
            node = node.next;
        }
        size++;
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * RESIZE_GROW_VALUE];
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            for (Node<K, V> node : oldTable) {
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
