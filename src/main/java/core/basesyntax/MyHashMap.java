package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int tableIndex = findIndex(key);
        Node<K, V> tempNode = new Node<>(key, value);
        if (table[tableIndex] == null) {
            table[tableIndex] = tempNode;
        } else if (isKeyPresent(tempNode)) {
            return;
        } else {
            Node<K, V> node = table[tableIndex];
            while (node.next != null) {
                node = node.next;
            }
            node.next = tempNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNodeTheByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * MULTIPLIER;
        threshold = (int)(newCapacity * LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            for (int i = 0; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int findIndex(K key) {
        if (key == null) {
            return 0;
        }
        return getHash(key) % table.length;
    }

    private boolean isKeyPresent(Node<K, V> node) {
        Node<K, V> tempNode = findNodeTheByKey(node.key);
        if (tempNode != null) {
            tempNode.value = node.value;
            return true;
        }
        return false;
    }

    private Node<K, V> findNodeTheByKey(K key) {
        int index = findIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
