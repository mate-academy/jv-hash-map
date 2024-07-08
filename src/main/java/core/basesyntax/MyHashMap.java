package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_INCREASE = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = getIndexForKey(key);
        }
        Node<K, V> currentNode = table[index];
        int hash = hashCode(key);
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hash, key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexForKey(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private int getIndexForKey(K key) {
        return hashCode(key) % table.length;
    }

    private void resize() {
        int newCapacity = table.length * CAPACITY_INCREASE;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int newIndex = hashCode(currentNode.key) % newCapacity;
                currentNode.next = newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = nextNode;
            }
        }
        table = newTable;
    }

    private int hashCode(Object key) {
        return (key == null) ? 0 : (key.hashCode() & 0x7fffffff);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
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
