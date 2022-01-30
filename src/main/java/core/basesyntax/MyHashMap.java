package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] currentTable;
    private int currentCapacity;
    private int threshHold;
    private int size;

    public MyHashMap() {
        this.currentCapacity = DEFAULT_CAPACITY;
        this.threshHold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.currentTable = (Node<K, V>[]) new Node[currentCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshHold) {
            resizeTable();
        }
        if (Objects.isNull(currentTable[getIndexByKey(key)])) {
            putFirstValue(key, value);
        } else {
            putNextValue(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = currentTable[getIndexByKey(key)];
        while (hasNext(currentNode)) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resizeTable() {
        currentCapacity += currentCapacity;
        threshHold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K, V>[] increasedEmptyTable = (Node<K, V>[]) new Node[currentCapacity];
        Node<K, V>[] oldTable = currentTable;
        currentTable = increasedEmptyTable;
        for (Node<K, V> oldNode : oldTable) {
            if (Objects.nonNull(oldNode)) {
                put(oldNode.key, oldNode.value);
                Node<K, V> temp = oldNode.next;
                while (hasNext(temp)) {
                    put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }
    }

    private boolean hasNext(Node<K, V> temp) {
        return Objects.nonNull(temp);
    }

    private int getHash(K key) {
        return Objects.isNull(key) ? 0 : key.hashCode();
    }

    private int getIndexByKey(K key) {
        return Objects.isNull(key) || key.hashCode() < 0 ? 0 : getHash(key) % currentCapacity;
    }

    private void putFirstValue(K key, V value) {
        currentTable[getIndexByKey(key)] = new Node<>(getHash(key), key, value, null);
        size++;
    }

    private void putNextValue(K key, V value) {
        Node<K, V> currentNode = currentTable[getIndexByKey(key)];
        if (Objects.isNull(currentNode)) {
            currentTable[getIndexByKey(key)] = new Node<>(getHash(key), key, value, null);
        } else {
            while (hasNext(currentNode)) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                } else if (!hasNext(currentNode.next)) {
                    currentNode.next = new Node<>(getHash(key), key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
