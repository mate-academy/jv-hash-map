package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] hashMapTable;
    private int size;

    public MyHashMap() {
        hashMapTable = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    private class Node<K,V> {
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

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            return result;
        }

    }

    @Override
    public void put(K key, V value) {
        Node<K, V> putNode = new Node<>(computeHash(key), key, value, null);
        Node<K, V> currentNode = hashMapTable[putNode.hash];
        do {
            if (currentNode == null) {
                hashMapTable[putNode.hash] = putNode;
                size++;
                extendCapacityIfNecessary();
                return;
            }
            if ((Objects.equals(currentNode.key, key) || (currentNode.key == null && key == null))
                    && hashMapTable[putNode.hash].next == null) {
                hashMapTable[putNode.hash] = putNode;
                //size++;
                return;
            }
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                //size++;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = putNode;
                size++;
                extendCapacityIfNecessary();

                return;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = hashMapTable[computeHash(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key) || (currentNode.key == null && key == null)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void extendCapacityIfNecessary() {
        if (size / DEFAULT_LOAD_FACTOR > hashMapTable.length) {
            Node<K, V>[] tempArray = (Node<K, V>[]) new Node[hashMapTable.length];
            tempArray = hashMapTable;
            hashMapTable = (Node<K, V>[]) new Node[hashMapTable.length * 2];
            for (int i = 0; i < tempArray.length; i++) {
                Node<K, V> tempNode = tempArray[i];
                while (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    size--;
                    tempNode = tempNode.next;
                }
            }
        }
    }

    private int computeHash(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key) % hashMapTable.length);
    }
}
