package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newElement = new Node<>(key, value, null);
        Node<K, V> element = table[index];
        if (element == null) {
            table[index] = newElement;
        }
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                element.value = value;
                return;
            }
            if (element.next == null) {
                element.next = newElement;
                break;
            }
            element = element.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> element = table[index];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    private int getIndex(K key) {
        return hash(key) & (table.length - 1);
    }

    private void resize() {
        if (threshold == size) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * 2];
            threshold *= 2;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
