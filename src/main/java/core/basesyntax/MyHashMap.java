package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final float GROW_FACTOR = 2f;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hashKey(K key) {
        return (key == null) ? 0 : (Math.abs(Objects.hash(key)) % table.length);
    }

    @SuppressWarnings("unchecked")
    final void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[(int) (table.length * GROW_FACTOR)];
        size = 0;
        for (Node<K, V> currentBucket : oldTable) {
            while (currentBucket != null) {
                put(currentBucket.key, currentBucket.value);
                currentBucket = currentBucket.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (table.length * LOAD_FACTOR < size) {
            resize();
        }
        int hashKeyNode = hashKey(key);
        int index = hashKeyNode & (table.length - 1);
        Node<K, V> currentNode = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(hashKeyNode, key, value, null);
        } else {
            while ((currentNode.key != key && !currentNode.key.equals(key))
                    && currentNode.next != null) {
                currentNode = currentNode.next;
            }
            if (currentNode.key == key || currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = new Node<>(hashKeyNode, key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexNode = hashKey(key);
        Node<K, V> currentNode = table[indexNode];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key.equals(key)) {
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

}
