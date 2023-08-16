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
        int index = Math.abs(hashCode % table.length);
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
        int index = Math.abs(hashCode % table.length);
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

    private static class Node<K, V> implements Map.Entry<K,V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = (key == null ? 0 : key.hashCode());
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                Node<K, V> next = oldNode.next;
                int newIndex = Math.abs(oldNode.hash % newCapacity);
                oldNode.next = newTable[newIndex];
                newTable[newIndex] = oldNode;
                oldNode = next;
            }
        }

        table = newTable;
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
