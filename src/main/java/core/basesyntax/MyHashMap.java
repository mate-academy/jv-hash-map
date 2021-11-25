package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] myTable;
    private int size;

    public MyHashMap() {
        myTable = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == myTable.length * DEFAULT_LOAD_FACTOR) {
            grow();
        }
        int index = getIndex(key);
        Node<K,V> currentNode = myTable[index];
        if (currentNode == null) {
            myTable[index] = new Node(key, value, null);
            size++;
        } else {
            while (currentNode != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> currentNode = myTable[getIndex(key)];
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

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % myTable.length);
    }

    private void grow() {
        size = 0;
        Node<K,V>[] currentTable = myTable;
        myTable = new Node[currentTable.length * 2];
        for (Node<K,V> node : currentTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
