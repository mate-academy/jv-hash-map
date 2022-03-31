package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] nodesArray;
    private int size;
    private int capacity;
    private final double loadFactor;
    private int threshold;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (loadFactor * capacity);
        nodesArray = new Node[capacity];
    }

    /**
     * Method add key and value to the hash list, or replace value if key exists
     * This method implemented by spliting on three mutually exclusive blocks.
     * Set block is ran to the last node in link which used in collision block, so we
     * don't need to iterate linked list twice.
     * @param key is Object that will be used to find value
     * @param value is Object that will be linked with key
     */
    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = hashIndexFromKey(key);
        Node<K, V> node = nodesArray[index];
        if (node == null) { //Add block
            nodesArray[index] = new Node<>(key, value);
            size++;
            return;
        }

        boolean isNotLast = true; //Set block
        while (isNotLast) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                isNotLast = false;
            } else {
                node = node.next;
            }

        }

        node.next = new Node<>(key, value); //Collision block
        size++;
    }

    private void insertNodeInNewNodeArray(Node<K, V>[] newNodeArray, Node<K, V> newNode) {
        int hashIndex = hashIndexFromKey(newNode.key);
        Node<K, V> storedNode = newNodeArray[hashIndex];
        if (storedNode == null) {
            newNodeArray[hashIndex] = newNode;
            return;
        }
        while (storedNode.next != null) {
            storedNode = storedNode.next;
        }
        storedNode.next = newNode;

    }

    @Override
    public V getValue(K key) {
        int index = hashIndexFromKey(key);
        Node<K, V> node = nodesArray[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        capacity = calculateNewCapacity();
        Node<K, V>[] newNodeArray = new Node[capacity];
        nodesArray = fillNewNodeArray(newNodeArray);
        threshold = (int) (capacity * loadFactor);
    }

    private Node<K, V>[] fillNewNodeArray(Node<K, V>[] newNodeArray) {
        for (Node<K, V> node : nodesArray) {
            while (node != null) {
                Node<K,V> next = node.next;
                node.next = null;
                insertNodeInNewNodeArray(newNodeArray, node);
                node = next;
            }
        }
        return newNodeArray;
    }

    private int hashIndexFromKey(K key) {
        int hash = Objects.hashCode(key);
        int hashIndex = hash % capacity;
        return hashIndex < 0 ? -hashIndex : hashIndex;
    }

    private int calculateNewCapacity() {
        return capacity << 1;
    }
}
