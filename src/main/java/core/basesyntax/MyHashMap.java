package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DOUBLING_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = (table.length - 1) & newNode.hash;
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (areKeysEqual(current.key, newNode.key)) {
                    changeValue(current, newNode);
                    return;
                } else {
                    current = current.next;
                }
            }
            if (areKeysEqual(current.key, newNode.key)) {
                changeValue(current, newNode);
                return;
            }
            current.next = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = (table.length - 1) & hash(key);
        if (index < table.length && table[index] != null) {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash(key)
                        && Objects.equals(current.key, key)) {
                    return current.value;
                } else {
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "table=" + Arrays.toString(table);
    }

    @SuppressWarnings({"unchecked"})
    private void resize() {
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[oldTable.length * DOUBLING_FACTOR];
        size = 0;
        transfer(oldTable);
    }

    private void appendNode(Node<K, V> current, Node<K, V> newNode) {
        current.next = newNode;
        size++;
    }

    private boolean areKeysEqual(K currentKey, K newKey) {
        return hash(currentKey) == hash(newKey)
                && (Objects.equals(currentKey, newKey));
    }

    private void changeValue(Node<K, V> current, Node<K, V> newNode) {
        current.value = newNode.value;
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> oldNode : oldTable) {
            if (oldNode != null) {
                Node<K, V> current = oldNode;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "{"
                    + "key=" + key
                    + ", value=" + value
                    + ", next=" + next
                    + "}";
        }
    }
}
