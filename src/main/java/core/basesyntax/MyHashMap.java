package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int capacity;
    private int threshold;
    private int size;
    private double loadFactor;
    private Node<K, V>[] table;
    private final int defaultInitialCapacity = 16;
    private final double defaultLoadFactor = 0.75f;

    public MyHashMap() {
        this.capacity = defaultInitialCapacity;
        this.threshold = (int) (capacity * defaultLoadFactor);
        this.loadFactor = defaultLoadFactor;
        this.table = new Node[defaultInitialCapacity];
    }

    public class Node<K, V> {
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

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }

    public int getHash(Object key) {
        int hash = 43;
        return (key == null) ? 0 : (hash * 17 + Objects.hashCode(key) ^ hash);
    }

    public boolean isKeyEqual(K key, Node<K, V> currentNode) {
        if (key == null && currentNode.getKey() == null) {
            return true;
        } else {
            return currentNode.getKey() != null && currentNode.getKey().equals(key);
        }
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
                j = Math.abs(getHash(kvNode.getKey()) % newCapacity);
                newTable[j] = new Node<>(kvNode.getHash(),
                        kvNode.getKey(), kvNode.getValue(), null);

                if (kvNode.getNext() != null) {
                    oldCurrent = kvNode.getNext();

                    while (oldCurrent != null) {
                        j = Math.abs(getHash(oldCurrent.getKey()) % newCapacity);

                        if (newTable[j] != null) {
                            newPrev = newTable[j];
                            newNext = newTable[j].getNext();
                            while (newNext != null) {
                                newPrev = newNext;
                                newNext = newPrev.getNext();
                            }
                            Node<K, V> newNode = new Node<>(oldCurrent.getHash(),
                                    oldCurrent.getKey(), oldCurrent.getValue(), null);
                            newPrev.setNext(newNode);
                        } else if (newTable[j] == null) {
                            newTable[j] = new Node<>(oldCurrent.getHash(),
                                    oldCurrent.getKey(), oldCurrent.getValue(), null);
                        }
                        oldCurrent = oldCurrent.getNext();
                    }
                }
            }
        }
        capacity = newCapacity;
        table = newTable;
        threshold = (int) (capacity * defaultLoadFactor);
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
                    currentNode.setValue(value);
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.getNext();
            }
            Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
            prevNode.setNext(newNode);
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
            return currentNode.getValue();
        } else if (currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
            while (currentNode != null) {
                if (isKeyEqual(key, currentNode)) {
                    return currentNode.getValue();
                }
                currentNode = currentNode.getNext();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
