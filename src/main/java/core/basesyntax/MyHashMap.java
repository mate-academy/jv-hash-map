package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    public int getHash(Object key) {
        int hash = 43;
        return (key == null) ? 0 : (hash * 17 + Objects.hashCode(key) ^ hash);
    }

    public boolean isKeyEqual(K key, Node<K, V> currentNode) {
        return (key == null && currentNode.key == null)
                || (currentNode.key != null && currentNode.key.equals(key));
    }

    public void resize() {
        int newCapacity = capacity * 2;
        int j;
        Node<K, V>[] newTable = new Node[newCapacity];
        Node<K, V> oldCurrent;
        Node<K, V> newNext;
        Node<K, V> newPrev;
        for (Node<K, V> kvNode : table) {
            if (kvNode != null) {
                j = Math.abs(getHash(kvNode.key) % newCapacity);
                newTable[j] = new Node<>(kvNode.hash,
                        kvNode.key, kvNode.value, null);
                if (kvNode.next != null) {
                    oldCurrent = kvNode.next;
                    while (oldCurrent != null) {
                        j = Math.abs(getHash(oldCurrent.key) % newCapacity);
                        if (newTable[j] != null) {
                            newPrev = newTable[j];
                            newNext = newTable[j].next;
                            while (newNext != null) {
                                newPrev = newNext;
                                newNext = newPrev.next;
                            }
                            Node<K, V> newNode = new Node<>(oldCurrent.hash,
                                    oldCurrent.key, oldCurrent.value, null);
                            newPrev.next = newNode;
                        } else if (newTable[j] == null) {
                            newTable[j] = new Node<>(oldCurrent.hash,
                                    oldCurrent.key, oldCurrent.value, null);
                        }
                        oldCurrent = oldCurrent.next;
                    }
                }
            }
        }
        capacity = newCapacity;
        table = newTable;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode;
        Node<K, V> prevNode;
        if (size >= threshold) {
            resize();
        }
        int i = Math.abs(getHash(key) % capacity);
        if (table[i] == null) {
            table[i] = new Node<>(getHash(key), key, value, null);
        } else {
            currentNode = table[i];
            prevNode = table[i];
            while (currentNode != null) {
                if (isKeyEqual(key, currentNode)) {
                    currentNode.value = value;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int i = Math.abs(getHash(key) % capacity);
        Node<K,V> currentNode = table[i];
        if (size == 0 || currentNode == null) {
            return null;
        }
        if (isKeyEqual(key, currentNode)) {
            return currentNode.value;
        } else if (currentNode.next != null) {
            currentNode = currentNode.next;
            while (currentNode != null) {
                if (isKeyEqual(key, currentNode)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
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
