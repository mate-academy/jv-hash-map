package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private Node<K,V>[] hashMapTable;
    private int size;
    private int threshold;

    public MyHashMap() {
        hashMapTable = new Node[DEFAULT_CAPACITY];
    }

    private class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        checkResize();
        Node<K, V> currentNode = hashMapTable[getIndex(key)];
        do {
            if (currentNode == null) {
                hashMapTable[getIndex(key)] = new Node<>(Objects.hash(key), key, value, null);
                size++;
                return;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(Objects.hash(key), key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = hashMapTable[getIndex(key)];
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

    private void checkResize() {
        threshold = (int) DEFAULT_LOAD_FACTOR * hashMapTable.length;
        if (threshold >= size) {
            Node<K, V>[] tempArray = hashMapTable;
            hashMapTable = (Node<K, V>[]) new Node[hashMapTable.length * INCREASE_COEFFICIENT];
            size = 0;
            for (int i = 0; i < tempArray.length; i++) {
                Node<K, V> tempNode = tempArray[i];
                while (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key) % hashMapTable.length);
    }
}
