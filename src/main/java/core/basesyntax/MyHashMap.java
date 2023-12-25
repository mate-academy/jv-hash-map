package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> node = table[index];
            while (node.key == null || !node.key.equals(key)) {
                if (node.key == null && key == null) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value);
                    size++;
                    break;
                }
                node = node.next;
            }
            if (node.key == null || node.key.equals(key)) {
                node.value = value;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode = table[getIndex(key)];
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

    public int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        size = 0;
        int newLength = table.length << 1;
        Node<K,V>[] oldTable = table;
        table = new Node[newLength];
        threshold = (int) (newLength * DEFAULT_LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return Math.abs(hash(key) % table.length);
    }
}
