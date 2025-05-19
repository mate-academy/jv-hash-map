package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    private int treeifyThreshold = 12;
    private Node<K,V>[] table;
    private int capacity;
    private int size;

    @SuppressWarnings({"unchecked"})
    private void resize() {
        size = 0;
        if (capacity >= MAXIMUM_CAPACITY) {
            return;
        }
        treeifyThreshold *= 2;
        capacity *= 2;
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[capacity];
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                Node<K, V> currentNode = kvNode;
                while (currentNode != null) {
                    put(currentNode.getKey(), currentNode.getValue());
                    currentNode = currentNode.next;
                }
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = (MyHashMap.Node<K,V>[])new MyHashMap.Node[DEFAULT_INITIAL_CAPACITY];
            capacity = DEFAULT_INITIAL_CAPACITY;
        }
        int hash = key == null ? 0 : key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode();
        int bucket = hash % capacity;
        Node<K,V> newNode = new Node<>(hash, key, value, null);
        if (size + 1 >= treeifyThreshold) {
            resize();
        }
        if (table[bucket] == null) {
            table[bucket] = newNode;
        } else {
            Node<K,V> currentNode = table[bucket];
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.getKey(), key)) {
                    currentNode.setValue(value);
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(currentNode.getKey(), key)) {
                currentNode.setValue(value);
                return;
            }
            currentNode.next = newNode;
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0) {
            return null;
        }
        int hash = key == null ? 0 : key.hashCode() < 0 ? key.hashCode() * -1 : key.hashCode();
        int bucket = hash % capacity;
        Node<K,V> currentNode = table[bucket];
        while (currentNode != null) {
            if (Objects.equals(currentNode.getKey(), key)) {
                return currentNode.getValue();
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K,V> implements Map.Entry<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private MyHashMap.Node<K,V> next;

        Node(int hash, K key, V value, MyHashMap.Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return o instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }
}
