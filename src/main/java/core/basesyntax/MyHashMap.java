package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> currentNode = table[getKeyIndex(key)];
        if (currentNode == null) {
            table[getKeyIndex(key)] = new Node<>(key, value, null);
        } else {
            while (currentNode.next != null
                    || Objects.equals(key, currentNode.key)) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getKeyIndex(key)];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K,V>[] tmpTable = table;
        int newCapacity = table.length * 2;
        threshold = newCapacity * 2;
        table = new Node[newCapacity];
        for (Node<K, V> node : tmpTable) {
            while (node != null) {
                put(node.key, node.value);
                size--;
                node = node.next;
            }
        }
    }

    private int getKeyIndex(K key) {
        return key != null ? Math.abs(key.hashCode()) % table.length : 0;
    }
}
