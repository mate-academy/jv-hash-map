package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] innerArray;
    private int size;

    public MyHashMap() {
        innerArray = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        grow();
        int index = getIndex(key);
        Node<K, V> tempNode = innerArray[index];
        if (tempNode == null) {
            innerArray[index] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> prevNode = tempNode;
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                tempNode.value = value;
                return;
            }
            prevNode = tempNode;
            tempNode = tempNode.next;
        }
        prevNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = innerArray[getIndex(key)];
        if (tempNode == null) {
            return null;
        }
        if (Objects.equals(tempNode.key, key)) {
            return tempNode.value;
        }
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(K key) {
        return getHash(key) % innerArray.length;
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return hash < 0 ? -hash : hash;
    }

    private void grow() {
        int length = innerArray.length;
        if (size >= length * DEFAULT_LOAD) {
            size = 0;
            Node<K, V>[] tempArray = new Node[length];
            System.arraycopy(innerArray, 0, tempArray, 0, length);
            innerArray = new Node[length << 1];
            for (Node<K, V> node : tempArray) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
