package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int RESIZE_FACTOR = 2;
    private static final int NEXT_ENTRY_SIZE = 1;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + NEXT_ENTRY_SIZE > threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int tableIndex = hash(key);
        if (table[tableIndex] != null) {
            Node<K, V> temp = table[tableIndex];
            Node<K, V> nextNode;
            if (Objects.equals(table[tableIndex].key, key)) {
                table[tableIndex] = newNode;
                table[tableIndex].next = temp.next;
                return;
            }
            while (temp.next != null) {
                if (hash(table[tableIndex].key) == hash(key)) {
                    if (Objects.equals(temp.next.key, key)) {
                        nextNode = temp.next;
                        temp.next = newNode;
                        newNode.next = nextNode;
                        return;
                    }
                    temp = temp.next;
                }
            }
            temp.next = newNode;
        } else {
            table[tableIndex] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int tableIndex = hash(key);
        if (table[tableIndex] == null) {
            return null;
        }
        if (table[tableIndex].next != null) {
            Node<K, V> temp = table[tableIndex];
            while (temp != null && !Objects.equals(temp.key, key)) {
                temp = temp.next;
            }
            if (temp == null) {
                return null;
            }
            return temp.value;
        }
        return table[tableIndex].value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int capacity = table.length;
        capacity *= RESIZE_FACTOR;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> temp = oldTable[i];
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value) ^ Objects.hashCode(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value) && Objects.equals(next, node.next);
        }
    }
}
