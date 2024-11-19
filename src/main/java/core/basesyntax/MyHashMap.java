package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    static int hash(Object key) {
        int keyHash;
        return (key == null) ? 0 : (keyHash = key.hashCode()) ^ (keyHash >>> 16);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table != null && table.length > 0) {
            int index = (table.length - 1) & hash;
            if (table[index] != null) {
                Node<K, V> currentNode = table[index];
                while (currentNode != null) {
                    if (currentNode.hash == hash
                            && currentNode.key == key
                            || key != null && key.equals(currentNode.key)) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = resize();
        }
        if (size >= threshold) {
            table = resize();
        }
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            while (true) {
                if (currentNode.hash == hash
                        && currentNode.key == key
                        || key != null && key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash, key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
        if (size >= threshold) {
            table = resize();
        }
    }

    Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity;

        if (oldCapacity >= DEFAULT_INITIAL_CAPACITY) {
            newCapacity = oldCapacity << 1;
            threshold = oldThreshold << 1;
        } else if (oldThreshold > 0) {
            newCapacity = oldThreshold;
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (threshold == 0) {
            threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        }
        table = (Node<K, V>[]) new Node[newCapacity];
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {
                Node<K, V> current = oldTable[i];
                while (current != null) {
                    Node<K, V> next = current.next;
                    int newIndex = current.hash & (newCapacity - 1);
                    current.next = table[newIndex];
                    table[newIndex] = current;
                    current = next;
                }
            }
        }
        return table;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

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
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
}
