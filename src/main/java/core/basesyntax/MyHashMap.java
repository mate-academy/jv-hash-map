package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MULTIPLICATION_INDEX = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o.getClass().equals(Node.class)) {
                Node current = (Node)o;
                return Objects.equals(this.key, current.key)
                        && Objects.equals(this.value, current.value)
                        && Objects.equals(this.next, current.next);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }

    @Override
    public void put(K key, V value) {
        growAndTransfer();
        int index = getIndex(getKeyHash(key));
        Node<K, V> endNode = table[index];
        if (endNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (endNode.next != null) {
            if (Objects.equals(endNode.key, key)) {
                endNode.value = value;
                return;
            }
            endNode = endNode.next;
        }
        if (Objects.equals(endNode.key, key)) {
            endNode.value = value;
            return;
        }
        endNode.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> helpNode = table[getIndex(getKeyHash(key))];
        while (helpNode != null) {
            if (Objects.equals(key, helpNode.key)) {
                return helpNode.value;
            }
            helpNode = helpNode.next;
        }
        return null;
    }

    @Override
    public boolean remove(K key) {
        Node<K, V> helpNode = table[getIndex(getKeyHash(key))];
        if (helpNode == null) {
            return false;
        }
        if (Objects.equals(helpNode.key, key)) {
            helpNode = helpNode.next;
            return true;
        }
        while (!Objects.equals(helpNode.next.key, key)) {
            helpNode = helpNode.next;
            if (Objects.equals(helpNode.next.key, key)) {
                helpNode.next = helpNode.next.next;
                return true;
            }
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void growAndTransfer() {
        if (size < threshold) {
            return;
        }
        size = 0;
        Node<K, V>[] bufferArray = table;
        table = new Node[bufferArray.length * MULTIPLICATION_INDEX];
        threshold = threshold * MULTIPLICATION_INDEX;
        for (Node<K, V> node : bufferArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getKeyHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndex(int hash) {
        return Math.abs(hash) % table.length;
    }
}

