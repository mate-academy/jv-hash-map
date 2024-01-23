package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double REHASHING_FACTOR = 0.75;
    private static final int REHASHING_SCALE = 2;
    private static final int DEFAULT_TABLE_SIZE = 16;
    private int tableSize = DEFAULT_TABLE_SIZE;
    private int elementsInTableNum = 0;
    // Wondering if there is a better way to initialize this nodes array
    @SuppressWarnings("unchecked")
    private Node<K, V>[] nodes = (Node<K, V>[]) new Node[tableSize];

    @Override
    public void put(final K keyToPut, final V valueToPut) {
        checkForRehash();
        // Return arrayLen if null key was passed, otherwise return normal index based on hashCode
        int index = calcIndex(keyToPut);

        // If cell is empty, save there, increse element counter
        if (nodes[index] == null) {
            // Put new node to the table
            nodes[index] = new Node<>(keyToPut, valueToPut);
            elementsInTableNum++;
        } else {
            Node<K, V> currentNode = nodes[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, keyToPut)) {
                    if (!Objects.equals(currentNode.value, valueToPut)) {
                        // Same keys but diff values, want to update that value or what
                        currentNode.value = valueToPut;
                        // Key is found and value is updated, proceed to return.
                    }
                    return;
                    // ELSE: There is already this node in the table you are to pointless action
                }

                // Condition to check if this is the last not null element of the loop
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(keyToPut, valueToPut);
                    elementsInTableNum++;
                    // Collision has happened, but we appended our node to the end of the list
                    return;
                }

                // Iterate though chained list
                currentNode = currentNode.next;
            }

        }
    }

    @Override
    public V getValue(final K keyToSearchBy) {
        Node<K, V> currentNode = nodes[calcIndex(keyToSearchBy)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, keyToSearchBy)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return elementsInTableNum;
    }

    private void rehash() {
        // Increase tableSize by REHASHING_SCALE
        tableSize *= REHASHING_SCALE;
        // Set size to zero, before rehashing and creating new table;
        elementsInTableNum = 0;

        @SuppressWarnings("unchecked")
        Node<K, V>[] newNodesArray = (Node<K, V>[]) new Node[tableSize];
        Node<K, V>[] oldNodesArray = nodes;
        nodes = newNodesArray;

        // Now we are iterating though oldNodes and putting them into newNodes list
        for (Node<K, V> oldNode : oldNodesArray) {
            Node<K, V> current = oldNode;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private void checkForRehash() {
        double fillPercent = (double) (elementsInTableNum + 1) / tableSize;
        if (fillPercent >= REHASHING_FACTOR) {
            rehash();
        }
    }

    private int calcIndex(final K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % tableSize;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String toString() {
            return "(Key, Value) -- (" + key + ", " + value + ")";
        }
    }
}
