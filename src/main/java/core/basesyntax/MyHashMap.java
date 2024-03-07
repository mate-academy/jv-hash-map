package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final float LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    private int size = 0;
    private int capacity;
    private int treshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
        treshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key) % capacity;
        if (table[index] == null) {
            table[index] = new Node(hash(key), key, value, null);
            size++;
        } else if (findNodeByKey(key, index) == null) {
            Node<K, V> newNode = new Node(hash(key), key, value, null);
            Node<K, V> lastNode = getLastNode(index);
            lastNode.next = newNode;
            size++;
        } else {
            Node<K, V> currentNode = findNodeByKey(key, index);
            currentNode.setValue(value);
        }

        if (size > treshold) {
            resize();
        }

    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % capacity;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.getKey(), key)) {
                return currentNode.getValue();
            } else {
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> findNodeByKey(K key, int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.getKey(), key)) {
                return currentNode;
            } else {
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    private void resize() {
        capacity = capacity * 2;
        treshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldNodes) {
        for (int i = 0; i < oldNodes.length; i++) {
            Node<K, V> currentNode = oldNodes[i];
            while (currentNode != null) {
                int newIndex = hash(currentNode.getKey()) % capacity;
                if (table[newIndex] == null) {
                    table[newIndex] = new Node(hash(currentNode.getKey()),
                            currentNode.getKey(), currentNode.getValue(), null);
                } else {
                    Node<K, V> newNode = new Node(hash(currentNode.getKey()),
                            currentNode.getKey(), currentNode.getValue(), null);
                    Node<K, V> lastNode = getLastNode(newIndex);
                    lastNode.next = newNode;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private Node<K, V> getLastNode(int index) {
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.next == null) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode() & 0x7FFFFFFF);
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }
}
