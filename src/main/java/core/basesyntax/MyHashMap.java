package core.basesyntax;

import static java.lang.Math.abs;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int number = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> currentNode = table[number];
        if (table[number] == null) {
            table[number] = newNode;
        } else {
            while (currentNode != null) {
                if (currentNode.key == null
                        && key == null || currentNode.key != null
                        && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            newNode.next = table[number].next;
            table[number].next = newNode;
        }
        size++;

    }

    @Override
    public V getValue(K key) {
        int number = getIndex(key);
        if (table[number] == null) {
            return null;
        }
        Node<K,V> newNode = table[number];
        while (newNode != null) {
            if (newNode.key == null && key == null
                    || newNode.key != null
                    && newNode.key.equals(key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        if (size < table.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        int number = 0;
        if (key != null) {
            number = abs(key.hashCode() % table.length);
        }
        return number;
    }

}
