package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int LENGTH_MULTIPLIER = 2;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        resize();
        int index = hashCode(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hashCode(key)];
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

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[table.length * LENGTH_MULTIPLIER];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hashCode(K key) {
        return (key == null) ? 0 : Math.abs(hashCode() % table.length);
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        private Node(K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
