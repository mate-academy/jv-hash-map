package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.size = 0;
        this.threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }

        if (key == null) {
            putNullValue(value);
            return;
        }
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] != null) {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        } else {
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            getNullValue();
        }
        int index = hash(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private V putNullValue(V value) {
        if (table[0] != null) {
            Node<K, V> currentNode = table[0];
            while (currentNode != null) {
                if (currentNode.next == null) {
                    currentNode.value = value;
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        } else {
            table[0] = new Node<>(null, value, null);
            size++;
        }
        return null;
    }

    private V getNullValue() {
        Node<K, V> currentNode = table[0];
        while (currentNode != null) {
            if (currentNode.key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K,V>[] oldTable = table;
        table = new Node[newCapacity];

        for (Node<K,V> node : oldTable) {
            while (node != null) {
                final Node<K,V> nextNode = node.next;
                node.next = null;
                int index = hash(node.key);
                node.next = table[index];
                table[index] = node;
                node = nextNode;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return (key != null ? !key.equals(node.key) : node.key != null)
                    && (value != null ? !value.equals(node.value) : node.value != null)
                    && (next != null ? next.equals(node.next) : node.next == null);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) * Objects.hashCode(value) * 31;
        }
    }
}
