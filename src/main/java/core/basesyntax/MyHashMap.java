package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int RESIZE_MULTIPLICATOR = 2;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int index;

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.hash = Math.abs(key == null ? 0 : Objects.hashCode(key));
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

    }

    @Override
    public void put(K key, V value) {
        table = size > threshold ? resize() : table;
        index = Math.abs(key == null ? 0 : key.hashCode() % capacity);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            for (Node<K, V> i = table[index]; i != null; i = i.next) {
                if (Objects.equals(i.key, key)) {
                    i.value = value;
                    return;
                }
                if (i.next == null) {
                    i.next = new Node<>(key, value, null);
                    size++;
                }
            }
        }
    }

    public void transfer(K key, V value, Node<K,V>[] t) {
        index = Math.abs(key == null ? 0 : key.hashCode() % capacity);
        if (t[index] == null) {
            t[index] = new Node<>(key, value, null);
        } else {
            for (Node<K, V> i = t[index]; i != null; i = i.next) {
                if (Objects.equals(i.key, key)) {
                    i.value = value;
                    return;
                }
                if (i.next == null) {
                    i.next = new Node<>(key, value, null);
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        index = Math.abs(key == null ? 0 : key.hashCode() % capacity);
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (Objects.equals(i.key, key)) {
                return i.value;
            }
        }
        return null;
    }

    final Node<K,V>[] resize() {
        Node<K, V>[] oldTab = table;
        int newCap = oldTab.length * RESIZE_MULTIPLICATOR;
        threshold = (int) (newCap * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTab = new Node[newCap];
        capacity = newCap;
        for (Node<K, V> entry : oldTab) {
            for (Node<K, V> i = entry; i != null; i = i.next) {
                transfer(i.key, i.value, newTab);
            }
        }
        return newTab;
    }

    @Override
    public int getSize() {
        return size;
    }

}

