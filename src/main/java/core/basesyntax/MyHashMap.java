package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_START_CAPACITY = 16;
    static final int INCREASE_CAPACITY_VALUE = 2;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K,V>[] nodes;

    public MyHashMap() {
        this.capacity = DEFAULT_START_CAPACITY;
        this.size = 0;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
        this.nodes = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resizeList(key, value);
            return;
        }
        int hashcode = hashCode(key);
        Node<K,V> newNode = new Node<>(key, value);
        if (key == null && nodes[0] == null) {
            nodes[0] = newNode;
            size++;
            return;
        } else if (key == null) {
            nodes[0] = newNode;
            return;
        }
        int index = defineIndex(hashcode);
        Node<K,V> lastNode = getLastCollisionNode(index);
        if (lastNode == null) {
            nodes[index] = newNode;
            size++;
        } else {
            reassignSameKeyValues(newNode, nodes[index]);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return nodes[0] != null ? nodes[0].getValue() : null;
        }
        int hashcode = hashCode(key);
        int index = defineIndex(hashcode);
        Node<K,V> firsNodeOnIndex = nodes[index];
        if (firsNodeOnIndex == null) {
            return null;
        }
        if (firsNodeOnIndex.next == null) {
            return firsNodeOnIndex.getValue();
        }
        Node<K,V> foundNode = getCollisionNode(firsNodeOnIndex, key);
        return foundNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode(K key) {
        int hashcode = Objects.hash(key);
        return hashcode < 0 ? (hashcode * -1) : hashcode;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        @Override
        public boolean equals(Object key) {
            return Objects.equals(key, this.key);
        }
    }

    private int defineIndex(int hashcode) {
        return hashcode % capacity;
    }

    private Node<K,V> getLastCollisionNode(int index) {
        if (nodes[index] == null) {
            return null;
        }
        Node<K,V> lastNode = nodes[index];
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        return lastNode;
    }

    private Node<K,V> getCollisionNode(Node<K,V> nextNode, K key) {
        if (nextNode.next != null && !nextNode.equals(key)) {
            nextNode = getCollisionNode(nextNode.next, key);
        }
        return nextNode;
    }

    private void reassignSameKeyValues(Node<K,V> newNode, Node<K,V> currentNode) {
        if (currentNode == null) {
            return;
        }
        K currentNodeKey = (K) currentNode.getKey();
        if (newNode.equals(currentNodeKey)) {
            currentNode.setValue(newNode.getValue());
            return;
        }
        if (currentNode.next == null) {
            currentNode.next = newNode;
            size++;
            return;
        }
        reassignSameKeyValues(newNode, currentNode.next);
    }

    private void resizeList(K key, V value) {
        this.capacity = capacity * INCREASE_CAPACITY_VALUE;
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * capacity);
        this.size = 0;
        Node<K,V>[] prevNodes = nodes;
        this.nodes = new Node[capacity];
        for (Node<K,V> node : prevNodes) {
            if (node != null) {
                reassignValues(node);
            }
        }
        put(key, value);
    }

    private void reassignValues(Node<K,V> node) {
        put(node.getKey(), node.getValue());
        if (node.next != null) {
            reassignValues(node.next);
        }
    }
}
