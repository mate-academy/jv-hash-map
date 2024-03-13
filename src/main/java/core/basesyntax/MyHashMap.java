package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_START_CAPACITY = 16;
    private int capacity;
    private int size;
    private int threshold;
    private Node[] nodes;

    public MyHashMap() {
        this.capacity = DEFAULT_START_CAPACITY;
        this.size = 0;
        this.threshold = (int) DEFAULT_LOAD_FACTOR * capacity;
        this.nodes = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hashcode = hashCode(key);
        Node newNode = new Node(key, value, hashcode);
        int index = defineIndex(hashcode);
        Node lastNode = getLastCollisionNode(index);
        if (lastNode == null) {
            nodes[index] = newNode;
        } else {
            lastNode.setNext(newNode);
        }
        size++;
    }

    // the we'll remove it
    @Override
    public String toString() {
        return "MyHashMap{" +
                "capacity=" + capacity +
                ", size=" + size +
                ", threshold=" + threshold +
                ", nodes=" + Arrays.toString(nodes) +
                '}';
    }
    // the we'll remove it

    @Override
    public V getValue(K key) {
        int hashcode = hashCode(key);
        int index = defineIndex(hashcode);
        Node firsNodeOnIndex = nodes[index];
        if (firsNodeOnIndex == null) {
            return null;
        }
        if (firsNodeOnIndex.next == null) {
            return (V) firsNodeOnIndex.getValue();
        }
        Node foundNode = getCollisionNode(firsNodeOnIndex, key);
        return foundNode == null ? null : (V) foundNode.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode(K key) {
        return Objects.hash(key);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private int hashcode;
        private Node<K, V> next;

        public Node(K key, V value, int hashcode) {
            this.key = key;
            this.value = value;
            this.hashcode = hashcode;
            this.next = null;
        }

        public V getValue() {
            return value;
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

    private Node getLastCollisionNode(int index) {
        if (nodes[index] == null) {
            return null;
        }
        Node lastNode = nodes[index];
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        return lastNode;
    }

    private Node getCollisionNode(Node nextNode, K key) {
        if (nextNode != null && !nextNode.equals(key)) {
            nextNode = getCollisionNode(nextNode.next, key);
        }
        return nextNode;
    }
}
