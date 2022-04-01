package core.basesyntax;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_THRESHOLD = 0.75;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_THRESHOLD) {
            resize();
        }
        int keyHash = hash(key);
        int indexOfBucket = keyHash % table.length;
        Node<K, V> current = table[indexOfBucket];
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node<>(key, value, null);
        }
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value, null);
                break;
            }
            current = current.next;
        }
        size++;
    }

    @Override
    public V remove(K key) {
        int indexOfBucket = hash(key) % table.length;
        Node<K, V> currentNode = table[indexOfBucket];
        Node<K, V> previousNode = currentNode;
        Node<K, V> nextNode;
        while (currentNode != null) {
            nextNode = currentNode.next;
            if (previousNode == currentNode && Objects.equals(currentNode.key, key)) {
                table[indexOfBucket] = currentNode.next;
                size--;
                return currentNode.value;
            } else if (Objects.equals(currentNode.key, key)) {
                previousNode.next = nextNode;
                size--;
                return currentNode.value;
            }
            previousNode = currentNode;
            currentNode = nextNode;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                keySet.add(currentNode.key);
                currentNode = currentNode.next;
            }
        }
        return keySet;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[oldTable.length * RESIZE_FACTOR];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
