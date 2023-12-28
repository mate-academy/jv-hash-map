package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DOUBLE_MULTIPLIER = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (size > 0 && replaceDuplicateKeyValue(key, value)) {
            return;
        }
        table = (size == table.length * DEFAULT_LOAD_FACTOR) ? resize() : table;
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);;
        if (current != null) {
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        } else {
            table[index] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (!checkElementIndexExistence(key)) {
            return null;
        }
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {

            if (Objects.equals(current.key, key)) {
                break;
            }
            current = current.next;
        }
        return current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K,V>[] resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DOUBLE_MULTIPLIER];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
                size--;
            }
        }
        return table;
    }

    private boolean replaceDuplicateKeyValue(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private boolean checkElementIndexExistence(K key) {
        if (size > 0) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && getIndex(table[i].key) == getIndex(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
