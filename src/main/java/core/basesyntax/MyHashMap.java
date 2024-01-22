package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double REHASHING_FACTOR = 0.75;
    private static final int REHASHING_SCALE = 2;
    private int tableSize = 15;
    private int elementsCount = 0;
    private Node<K, V>[] nodes = (Node<K, V>[]) new Node[tableSize];

    @Override
    public void put(K key, V value) {
        checkForRehash();

//        int indexOfKey = calcIndex(key);
        if (containsKey(key)) {
            Node<K, V> nodeWithCollision = new Node<>(key, value);
            nodes[indexOfKey].next =  nodeWithCollision;
            elementsCount++;
        }
        else if (indexOfKey == -1) {
            int indexWhereInsertEntity = key.hashCode() % tableSize;
            nodes[indexWhereInsertEntity] = new Node<>(key, value);
            elementsCount++;
        } else {
            throw new RuntimeException("Variable indexOfKey should be -1 or >=0. But indexOfKey -- " + indexOfKey);
        }
    }

    public V set(K key, V value) {
        if (containsKey(key)) {
            int indexOfKey = calcIndex(key);
            System.out.println("Value in the table will be updated:\n" +  nodes[indexOfKey] + " new value " + value);
            V oldValue = nodes[indexOfKey].value;
            nodes[indexOfKey].value = value;
            return oldValue;
        }
        else {
            throw new RuntimeException("Value by this key is absent in table " + key);
        }

    }

    @Override
    public V getValue(K key) {
        Node<K, V> foundNode = nodes[calcIndex(key)];
        if (foundNode != null) {
            return foundNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return elementsCount;
    }

    private Node<K, V> containsKey(K keyToFind) {

        // Cell is occupied, but your key is not in table, it is just collision
        return cur;
    }

    private void checkForRehash() {
        if ( (double) (elementsCount + 1) / tableSize > REHASHING_FACTOR) {
            System.out.println(Arrays.toString(nodes));
            int newSize = (int)tableSize*REHASHING_SCALE;

            Node<K, V>[] newNodesArray = (Node<K, V>[]) new Node[newSize];
            for (Node<K, V> node : nodes) {
                newNodesArray[calcIndex(node.key)] = node;
            }

            nodes = newNodesArray;
            System.out.println(Arrays.toString(nodes));
            System.out.println("Rehashing table to 2x size");
        }
    }

    private int calcIndex(K key) {
        return key.hashCode() % tableSize;
    }
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String toString() {
            return "(Key, Value) -- (" + key + ", " + value + ")";
        }
    }
}
