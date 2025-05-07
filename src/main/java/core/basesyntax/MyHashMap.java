package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int position = getPosition(key, table.length);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[position];
        if (table[position] == null) {
            table[position] = newNode;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key,key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = getPosition(key, table.length);
        Node<K,V> currentNode = table[position];
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

    private int getPosition(Object key, int capacity) {
        int position = key == null ? 0 : key.hashCode() % capacity;
        return Math.abs(position);
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
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
