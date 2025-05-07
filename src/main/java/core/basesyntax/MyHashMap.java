package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        putValue(getIndex(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
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
        Node<K, V>[] oldTable = table;
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = new Node[newCapacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putValue(getIndex(node.key), node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(key, value, null);
                size++;
            }
        }
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
}
