package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = .75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        float threshold = table.length * LOAD_FACTOR;

        if (size > threshold) {
            resize();
        }

        int hashCode = (key == null ? 0 : key.hashCode());
        int index = getIndex(hashCode, table.length);
        Node<K, V> currentNode = table[index];

        if (!replaceValue(currentNode, key, value)) {
            Node<K, V> newNode = new Node<>(key, value);
            newNode.next = currentNode;
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hashCode = (key == null ? 0 : key.hashCode());
        int index = getIndex(hashCode, table.length);
        Node<K, V> node = table[index];

        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
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

    private static class Node<K, V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = (key == null ? 0 : key.hashCode());
        }
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] newTable = new Node[newCapacity];
        Node<K, V>[] oldTable = table;
        table = newTable;
        size = 0;

        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                K key = oldNode.key;
                V value = oldNode.value;
                put(key, value);
                oldNode = oldNode.next;
            }
        }
    }

    private int getIndex(int hashCode, int arrayLength) {
        return Math.abs(hashCode % arrayLength);
    }

    private boolean replaceValue(Node<K, V> node, K key, V value) {
        while (node != null) {
            K currentKey = node.key;
            if (currentKey == key || (currentKey != null && currentKey.equals(key))) {
                node.value = value;
                return true;
            }
            node = node.next;
        }

        return false;
    }
}
