package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int SIZE_INCREASE = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

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
            newNode = new Node<>(key, value, null);
        } else {
            newNode = new Node<>(key, value, null);
        }
        int position = getPosition(key);
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
        int position = getPosition(key);
        if (table[position] == null) {
            return null;
        }
        Node<K, V> itemInTable = table[position];
        while (itemInTable != null) {
            if (Objects.equals(itemInTable.key, key)) {
                return itemInTable.value;
            }
            itemInTable = itemInTable.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        capacity *= SIZE_INCREASE;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTab = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> oldNode : oldTab) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private int getPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
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
