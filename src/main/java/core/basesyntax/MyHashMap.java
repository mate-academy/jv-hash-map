package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double OVERLOADING_FACTOR = 0.75;
    private static final int INITIAL_SIZE = 16;
    private int size;
    private Node<K, V>[] array = new Node[INITIAL_SIZE];

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = (key == null ? 0 : key.hashCode());
            if (hash < 0) {
                hash = hash * -1;
            }
            this.key = key;
            this.value = value;
            this.next = null;
        }

    }

    @Override
    public void put(K key, V value) {
        checkForResize();
        Node<K, V> newNode = new Node<>(key, value);
        int currentBucketNumber = newNode.hash % array.length;
        if (array[currentBucketNumber] == null) {
            array[currentBucketNumber] = newNode;
            size++;
        } else {
            Node<K, V> currentBucketNode = array[currentBucketNumber];
            if (Objects.equals(currentBucketNode.key, newNode.key)) {
                Node<K, V> nextNode = array[currentBucketNumber].next;
                array[currentBucketNumber] = newNode;
                newNode.next = nextNode;
            } else {
                boolean sameKeyWasFound = false;
                while (currentBucketNode.next != null) {
                    if (Objects.equals(currentBucketNode.next.key, newNode.key)) {
                        newNode.next = currentBucketNode.next.next;
                        currentBucketNode.next = newNode;
                        sameKeyWasFound = true;
                        break;
                    }
                    currentBucketNode = currentBucketNode.next;
                }
                if (!sameKeyWasFound) {
                    currentBucketNode.next = newNode;
                    size++;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = key == null ? 0 : key.hashCode();
        if (hash < 0) {
            hash = hash * -1;
        }
        Node<K, V> currentBucket = array[hash % array.length];
        while (currentBucket != null) {
            if (Objects.equals(currentBucket.key, key)) {
                return currentBucket.value;
            }
            currentBucket = currentBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkForResize() {
        if (size > array.length * OVERLOADING_FACTOR) {
            Node<K, V>[] oldArray = array;
            array = new Node[oldArray.length * 2];
            size = 0;
            for (int i = 0; i < oldArray.length; i++) {
                if (oldArray[i] != null) {
                    Node<K, V> node = oldArray[i];
                    while (node != null) {
                        put(node.key, node.value);
                        node = node.next;
                    }
                }
            }
        }
    }
}
