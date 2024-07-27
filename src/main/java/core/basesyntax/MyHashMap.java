package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int BIT_MASK = 0x7FFFFFFF;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
        int index = (hash(key) & BIT_MASK) % table.length;
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> existingNode = table[index];
        if (existingNode == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = existingNode;
            while (true) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (hash(key) & BIT_MASK) % table.length;
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() & BIT_MASK;
    }

    private void resize() {
        int newCapacity = table.length * DEFAULT_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = (hash(node.key) & BIT_MASK) % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
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
