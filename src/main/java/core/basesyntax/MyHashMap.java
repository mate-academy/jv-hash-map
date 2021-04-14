package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private double threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public int hashCode() {
            hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K,V> newNode = new Node(key,value);
        int index = putIndex(newNode);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        } else if (table[index].value != null) {
            if (Objects.equals(table[index].key, key)) {
                table[index].value = value;
                return;
            } else {
                Node<K, V> currentNode = table[index];
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.next = newNode;
                size++;
                return;
            }
        }

    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Node<K,V> node) {
        if (node.key == null) {
            return 0;
        }
        return node.hashCode() % table.length;
    }

    private int putIndex(Node<K,V> node) {
        return Math.abs(hash(node));
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        threshold = (int) table.length * DEFAULT_LOAD_FACTOR;
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
