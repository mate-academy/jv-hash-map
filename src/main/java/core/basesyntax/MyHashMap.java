package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode;
        if (key == null) {
            newNode = new Node<>(0, key, value, null);
        } else {
            newNode = new Node<>(key.hashCode(), key, value, null);
        }
        int position = hash(key);
        if (table[position] == null) {
            table[position] = newNode;
        } else if (Objects.equals(table[position].key, key)) {
            table[position].value = newNode.value;
            return;
        } else {
            Node<K, V> itemInTable = table[position];
            while (itemInTable.next != null) {
                itemInTable = itemInTable.next;
                if (Objects.equals(itemInTable.key, key)) {
                    itemInTable.value = newNode.value;
                    return;
                }
            }
            itemInTable.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = hash(key);
        if (table[position] == null) {
            return null;
        }
        if (Objects.equals(table[position].key, key)) {
            return table[position].value;
        } else {
            Node<K, V> itemInTable = table[position];
            while (itemInTable.next != null) {
                itemInTable = itemInTable.next;
                if (Objects.equals(itemInTable.key, key)) {
                    return itemInTable.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        size = 0;
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTab = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> kvNode : oldTab) {
            if (kvNode != null) {
                Node<K, V> nextItem = kvNode.next;
                put(kvNode.key, kvNode.value);
                while (nextItem != null) {
                    put(nextItem.key, nextItem.value);
                    nextItem = nextItem.next;
                }
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> implements Map.Entry<K, V> {
        protected int hash;
        protected K key;
        protected V value;
        protected Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
