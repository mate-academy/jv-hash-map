package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] keyValueStorage;

    public MyHashMap() {
        keyValueStorage = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> myNode = keyValueStorage[getIndex(key)];
        if (myNode == null) {
            keyValueStorage[getIndex(key)] = new Node(null, key, value);
            size++;
            return;
        }
        while (myNode != null) {
            if (Objects.equals(key, myNode.key)) {
                myNode.value = value;
                return;
            }
            if (myNode.next == null) {
                myNode.next = new Node(null, key, value);
                size++;
                return;
            }
            myNode = myNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> myNode = keyValueStorage[getIndex(key)];
        while (myNode != null) {
            if (Objects.equals(myNode.key, key)) {
                return myNode.value;
            }
            myNode = myNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size >= keyValueStorage.length * LOAD_FACTOR) {
            Node<K, V>[] newKeysValue = new Node[keyValueStorage.length * 2];
            Node<K, V>[] oldKeysValue = keyValueStorage;
            keyValueStorage = newKeysValue;
            size = 0;
            for (Node<K, V> myNode : oldKeysValue) {
                while (myNode != null) {
                    put(myNode.key, myNode.value);
                    myNode = myNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % keyValueStorage.length);
    }

    private static class Node<K, V> {
        private V value;
        private K key;
        private Node<K, V> next;

        Node(Node<K, V> next, K key, V value) {
            this.key = key;
            this.next = next;
            this.value = value;
        }
    }
}
