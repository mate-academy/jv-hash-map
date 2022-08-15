package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int INCREASE_CAPACITY = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> current = table[getIndex(key)];
        if (current == null) {
            table[getIndex(key)] = newNode;
            size++;
        } else {
            while (current != null) {
                if (Objects.equals(current.key, newNode.key)) {
                    current.value = newNode.value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndex(key)];
        while (current != null && !Objects.equals(current.key, key)) {
            current = current.next;
        }
        return current == null ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        for (Node<K, V> current : table) {
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (Node<K, V> current : table) {
            while (current != null) {
                if (Objects.equals(current.value, value)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * INCREASE_CAPACITY];
        size = 0;
        for (Node<K, V> current : oldTable) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getIndex(K key) {
        return getHashcode(key) % table.length;
    }

    private int getHashcode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
