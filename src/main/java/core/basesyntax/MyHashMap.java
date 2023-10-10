package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final int GROW_VALUE = 2;
    public static final float LOAD_FACTOR = 0.75f;

    private int size;
    private Node<K, V>[] mapArray;

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            mapArray = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        }
        if (putVal(key, value, mapArray)) {
            if (++size > mapArray.length * LOAD_FACTOR) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (mapArray == null) {
            return null;
        }
        int pos = calcPos(key, mapArray);
        Node<K, V> currentNode = mapArray[pos];
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

    private void resize() {
        Node<K, V>[] newMap = (Node<K, V>[]) new Node[mapArray.length * GROW_VALUE];
        for (Node<K, V> node : mapArray) {
            if (node != null) {
                Node<K, V> tempNode = node;
                while (tempNode != null) {
                    putVal(tempNode.key, tempNode.value, newMap);
                    tempNode = tempNode.next;
                }
            }
        }
        mapArray = newMap.clone();
    }

    private int calcPos(K key, Node<K, V>[] array) {
        int pos = key == null ? 0 : key.hashCode() % array.length;
        if (pos < 0) {
            pos = -pos;
        }
        return pos;
    }

    private boolean putVal(K key, V value, Node<K, V>[] nodeArray) {
        int pos = calcPos(key, nodeArray);
        Node<K, V> currentNode = nodeArray[pos];

        while (currentNode != null && currentNode.next != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return false;
            }
            currentNode = currentNode.next;
        }

        if (currentNode == null) {
            currentNode = new Node<>(key, value);
            nodeArray[pos] = currentNode;
        } else {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return false;
            }
            currentNode.next = new Node<>(key, value);
        }
        return true;
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            hash = key == null ? 0 : key.hashCode();
        }
    }
}
