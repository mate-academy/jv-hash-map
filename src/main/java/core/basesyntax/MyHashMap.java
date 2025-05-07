package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> currentNode =    table[hash(key)];
        if (currentNode == null) {
            table[hash(key)] = new Node<>(key, value, null);
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                break;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length << 1];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                K key = node.key;
                V value = node.value;
                put(key, value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
