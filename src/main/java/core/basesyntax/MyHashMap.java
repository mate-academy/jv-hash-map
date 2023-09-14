package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkLoad();
        int hash = hash(key, table.length);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key, table.length);
        Node<K, V> currentNode = table[hash];
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

    private void resize(Node<K, V>[] array) {
        Node<K, V>[] newTable = new Node[array.length * 2];

        for (Node<K, V> element : table) {
            while (element != null) {
                Node<K, V> next = element.next;
                int newHash = hash(element.key, newTable.length);
                element.next = newTable[newHash];
                newTable[newHash] = element;
                element = next;
            }
        }
        table = newTable;
    }

    private int hash(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode()) % length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void checkLoad() {
        if (size >= table.length * LOAD_FACTOR) {
            resize(table);
        }
    }
}
