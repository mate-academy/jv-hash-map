package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MULTIPLY = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            resize();
        } else {
            Node<K, V> currentNode = table[index];
            for (int i = 0; i < size; i++) {
                if (currentNode.equals(newNode)) {
                    currentNode.value = newNode.value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    resize();
                    return;
                }
                currentNode = currentNode.next;
            }
        }

    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        for (int i = 0; i < size; i++) {
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || o.getClass() != getClass()) {
                return false;
            }
            Node<K, V> currentNode = (Node) o;
            return key == currentNode.key || key != null && key.equals(currentNode.key);
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : hash(key) % table.length;
    }

    private Node<K, V>[] resize() {
        final Node<K, V>[] oldTable = table;
        int oldCapacity = table.length;
        int oldThreshold = (int) (oldCapacity * LOAD_FACTOR);
        if (size == oldThreshold) {
            int newCapacity = oldCapacity * MULTIPLY;
            table = new Node[newCapacity];
            size = 0;
            transfer(oldTable);
        } else {
            return oldTable;
        }
        return table;
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
    }
}
