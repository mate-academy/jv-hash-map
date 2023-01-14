package core.basesyntax;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashTable;
    private int threshold = DEFAULT_INITIAL_CAPACITY * (int) DEFAULT_LOAD_FACTOR;
    private int size;

    public MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold || hashTable == null) {
            resize();
        }
        putValue(hash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (hashTable != null) {
            int index = hash(key);
            Node<K, V> currentNode = hashTable[index];
            while (currentNode != null) {
                if (Objects.equals(key, currentNode.getKey())) {
                    return currentNode.getValue();
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
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
    }

    private void putValue(int hash, K key, V value) {
        if (hashTable[hash] == null) {
            hashTable[hash] = new Node<>(key, value);
        } else {
            Node<K, V> currentNode = hashTable[hash];
            while (currentNode.next != null) {
                if (Objects.equals(key, currentNode.getKey())) {
                    break;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(key, currentNode.getKey())) {
                currentNode.setValue(value);
                size--;
            } else {
                currentNode.next = new Node<>(key, value);
            }
        }
    }

    private void resize() {
        if (hashTable == null) {
            hashTable = new Node[DEFAULT_INITIAL_CAPACITY];
        }
        int newCapacity = hashTable.length << 1;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[newCapacity];
        transfer(oldHashTable);
    }

    private void transfer(Node<K, V>[] oldHashTable) {
        for (Node<K, V> node : oldHashTable) {
            Node<K, V> newNode = node;
            while (newNode != null) {
                putValue(hash(newNode.getKey()), newNode.getKey(), newNode.getValue());
                newNode = newNode.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % hashTable.length);
    }
}
