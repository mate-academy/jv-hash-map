package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_SIZE = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        capacity = INITIAL_SIZE;
        table = new Node[INITIAL_SIZE];
    }

    private int getIndexForBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

//    private int getIndexForBucket(Object key, int capacity) {
//        return key == null ? 0 : key.hashCode() & (capacity - 1);
//    }

    @Override
    public V getValue(K key) {
        int index = getIndexForBucket(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        int bucketIndex = getIndexForBucket(key);
        Node<K,V> currentNode = table[bucketIndex];
        if (currentNode == null) {
            table[bucketIndex] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (currentNode.next != null) {
                if (checkEquality(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (checkEquality(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(hash(key), key, value, null);
                size++;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K,V> [] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (capacity * 0.75f);
        threshold = threshold * 2;
        transfer(table, newTable);
        table = newTable;
        capacity = newCapacity;
    }

//    private void resize() {
//        int GROW_FACTOR = 2;
//        Node<K,V> [] newTable = new Node[table.length * GROW_FACTOR];
//        transfer(table, newTable);
//        table = newTable;
//    }

    private void transfer(Node<K,V> [] oldTable, Node<K,V> [] newTable) {
        int index = 0;
        while (index < oldTable.length) {
            Node<K,V> currentNode = oldTable[index];
            if (currentNode != null) {
                do {
                    oldTable[index] = null;
                    int bucketIndex = getIndexForBucket(currentNode.key);
                    Node<K, V> next = currentNode.next;
                    currentNode.next = newTable[bucketIndex];
                    newTable[bucketIndex] = currentNode;
                    currentNode = next;
                } while (currentNode != null);
            }
            index++;
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private boolean checkEquality(K firstElement, K secondElement) {
        return firstElement == secondElement
                || firstElement != null && firstElement.equals(secondElement);
    }

    private static class Node<K, V> {
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
}
