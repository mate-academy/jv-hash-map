package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == (int) (table.length * LOAD_FACTOR)) {
            resizeArray();
        }
        int index = getIndex();
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> nodeByIndex = table[index];
        while (nodeByIndex != null) {
            if (Objects.equals(key, nodeByIndex.key)) {
                nodeByIndex.value = value;
                return;
            }
            if (nodeByIndex.next == null) {
                nodeByIndex.next = newNode;
                size++;
                return;
            }
            nodeByIndex = nodeByIndex.next;
        }
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex();
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (Objects.equals(key, newNode.key)) {
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

    private int getIndex() {
        return hashCode() % table.length;
    }

    private void resizeArray() {
        int arraySize = table.length * 2;
        Node<K, V>[] newArray = new Node[arraySize];
        transfer(newArray);
        table = newArray;
    }

    private void transfer(Node<K, V>[] newArray) {
        for (int i = 0; i < table.length;i++) {
            Node<K, V> node = table[i];
            table[i] = null;
            while (node != null) {
                Node<K, V> next = node.next;
                int index = hashCode() % newArray.length;
                node.next = newArray[index];
                newArray[index] = node;
                node = next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return key != null ? 9 * key.hashCode() * value.hashCode() : 0;
        }
    }
}
