package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_MULTIPLAYER = 2;
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
        int indexOfBucked = getIndexOfBucked(key);
        Node<K, V> currentNode = table[indexOfBucked];
        if (currentNode == null) {
            table[indexOfBucked] = new Node<>(key, value, null);
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key,value,null);
                break;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_MULTIPLAYER];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfBucked = getIndexOfBucked(key);
        Node<K, V> node = table[indexOfBucked];
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

    private int getIndexOfBucked(K key) {
        return Math.abs(Objects.hashCode(key)) % table.length;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
