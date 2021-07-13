package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        currentCapacity = INITIAL_CAPACITY;
        table = new Node[currentCapacity];
        size = 0;
    }

    private int getIndexByKey(K key) {
        int hash = Math.max(0, key == null ? 0 : key.hashCode());
        return hash % currentCapacity;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexByKey(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        /* Can't decide which is better.
        1:
        while (true) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        2:
        */
        if (Objects.equals(key, currentNode.key)) {
            currentNode.value = value;
            return;
        }
        while (currentNode.next != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        currentNode.next = new Node<>(key, value);
        size++;
    }

    private void resize() {
        if (size < currentCapacity * LOAD_FACTOR) {
            return;
        }
        currentCapacity *= 2;
        Node<K, V>[] prevTable = table;
        table = new Node[currentCapacity];
        size = 0;
        for (Node<K, V> node : prevTable) {
            Node<K, V> innerNode = node;
            while (innerNode != null) {
                put(innerNode.key, innerNode.value);
                innerNode = innerNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndexByKey(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    private class Node<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node (K key, V value) {
            this.hash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
        }
    }
}
