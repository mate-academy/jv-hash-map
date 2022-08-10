package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_VALUE = 2;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        if (table[indexOf(key)] == null) {
            table[indexOf(key)] = node;
            size++;
        } else {
            Node<K, V> currentNode = table[indexOf(key)];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = node;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[indexOf(key)];
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

    private int indexOf(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * RESIZE_VALUE;
        threshold = (int) (LOAD_FACTOR * newCapacity);
        table = new Node[newCapacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
