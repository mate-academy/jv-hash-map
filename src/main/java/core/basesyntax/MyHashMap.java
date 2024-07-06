package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        int index;
        if (size >= threshold) {
            resize();
        }
        if (key == null) {
            index = 0;
        } else {
            index = hashCode(key) % table.length;
        }
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hashCode(key), key, value, null);
            size++;
            return;
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hashCode(key), key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }

    }

    @Override
    public V getValue(K key) {
        int index = hashCode(key) % table.length;
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        while (currentNode.next != null) {
            if (Objects.equals(currentNode.key,key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        if (Objects.equals(currentNode.key,key)) {
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int newIndex = currentNode.key.hashCode() % newCapacity;
                currentNode.next = newTable[newIndex];
                newTable[newIndex] = currentNode;
                currentNode = nextNode;
            }
        }
        table = newTable;
    }

    private int hashCode(Object kay) {
        return (kay == null) ? 0 : kay.hashCode();
    }

    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
