package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K,V> previousNode = table[index];
            while (previousNode != null) {
                if (Objects.equals(previousNode.key, key)) {
                    previousNode.value = value;
                    return;
                }
                if (previousNode.next == null) {
                    previousNode.next = newNode;
                    break;
                } else {
                    previousNode = previousNode.next;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
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

    private void resize() {
        Node<K, V>[] previousTable = table;
        table = new Node[previousTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node: previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
