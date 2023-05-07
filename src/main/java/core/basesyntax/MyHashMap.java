package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        isNeedToResize();
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prevNode = null;
            Node<K, V> curNode = table[index];
            while (curNode != null) {
                if (Objects.equals(key, curNode.key)) {
                    curNode.value = value;
                    return;
                }
                prevNode = curNode;
                curNode = curNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> findNode = table[index];
        while (findNode != null) {
            if (Objects.equals(findNode.key, key)) {
                return findNode.value;
            }
            findNode = findNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K keyObject) {
        return keyObject == null ? 0 : Math.abs(keyObject.hashCode() % table.length);
    }

    private int hash(K keyObject, int divider) {
        return keyObject == null ? 0 : Math.abs(keyObject.hashCode() % divider);
    }

    private void isNeedToResize() {
        if (size == threshold) {
            int newCapacity = table.length * 2;
            Node<K, V>[] newTable = new Node[newCapacity];
            for (int i = 0; i < table.length; i++) {
                while (table[i] != null) {
                    int index = hash(table[i].key, newCapacity);
                    Node<K, V> next = table[i].next;
                    table[i].next = newTable[index];
                    newTable[index] = table[i];
                    table[i] = next;
                }
            }
            threshold = (int) (newTable.length * LOAD_FACTOR);
            table = newTable;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
