package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int tableSize = 0;
    private int threshold;

    static class Node<K, V> {
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

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final boolean equals(Object current) {
            if (current == this) {
                return true;
            }

            if (current == null) {
                return false;
            }

            return current instanceof Node<?, ?> origin
                    && Objects.equals(key, origin.getKey())
                    && Objects.equals(value, origin.getValue());
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    public final int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    final Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity;
        int newThreshold = 0;

        if (oldCapacity > 0) {
            if (oldCapacity >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTable;
            } else if ((newCapacity = oldCapacity << 1) < MAXIMUM_CAPACITY
                    && oldCapacity >= DEFAULT_INITIAL_CAPACITY) {
                newThreshold = oldThreshold << 1;
            }
        } else if (oldThreshold > 0) {
            newCapacity = oldThreshold;
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        if (newThreshold == 0) {
            float resizeThreshold = (float) newCapacity * 0.75f;
            newThreshold = (newCapacity < MAXIMUM_CAPACITY
                    && resizeThreshold < (float) MAXIMUM_CAPACITY
                    ? (int) resizeThreshold : Integer.MAX_VALUE);
        }
        threshold = newThreshold;

        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        table = newTable;
        if (oldTable != null) {
            for (int j = 0; j < oldCapacity; ++j) {
                Node<K, V> element;
                if ((element = oldTable[j]) != null) {
                    oldTable[j] = null;
                    do {
                        Node<K, V> next = element.next;
                        int index = element.hash & (newCapacity - 1);
                        element.next = newTable[index];
                        newTable[index] = element;
                        element = next;
                    } while (element != null);
                }
            }
        }
        return newTable;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V>[] tableArray;
        Node<K, V> currentNode;
        int currentCapacity;
        int index;

        if ((tableArray = table) == null || (currentCapacity = tableArray.length) == 0) {
            currentCapacity = (tableArray = resize()).length;
        }

        index = (currentCapacity - 1) & hash(key);
        currentNode = tableArray[index];

        if (currentNode == null) {
            tableArray[index] = new Node(hash(key), key, value, null);
            tableSize++;
        } else {
            Node<K, V> element;
            K existingKey;

            if (currentNode.hash == hash(key) && ((existingKey = currentNode.key) == key
                    || (key != null && key.equals(existingKey)))) {
                currentNode.value = value;
            } else {
                for (; ; ) {
                    if ((element = currentNode.next) == null) {
                        currentNode.next = new Node<>(hash(key), key, value, null);
                        tableSize++;
                        break;
                    }

                    if (element.hash == hash(key) && ((existingKey = element.key) == key
                            || (key != null && key.equals(existingKey)))) {
                        element.value = value;
                        return;
                    }
                    currentNode = element;
                }
            }
        }

        if (tableSize > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : (hash(key) & (table.length - 1));
        Node<K, V> element = table[index];

        while (element != null) {
            if (Objects.equals(key, element.getKey())) {
                return element.getValue();
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return tableSize;
    }
}
