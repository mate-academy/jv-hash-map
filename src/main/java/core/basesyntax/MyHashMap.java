package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] keysValue;

    public MyHashMap() {
        keysValue = new Node[CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        chekSize();
        Node<K, V> myNode = keysValue[index(key)];
        if (myNode == null) {
            keysValue[index(key)] = new Node(null, key, value);
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
        Node<K, V> myNode = keysValue[index(key)];
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

    private void chekSize() {
        if (size >= keysValue.length * LOAD_FACTOR) {
            Node<K, V>[] newKeysValue = new Node[keysValue.length * 2];
            Node<K, V>[] oldKeysValue = keysValue;
            keysValue = newKeysValue;
            size = 0;
            for (Node<K, V> myNode : oldKeysValue) {
                while (myNode != null) {
                    put(myNode.key, myNode.value);
                    myNode = myNode.next;
                }
            }
        }
    }

    private int index(K key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode() % keysValue.length);
        }
        return index;
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
