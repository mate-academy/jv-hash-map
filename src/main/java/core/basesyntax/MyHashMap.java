package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_GROW_SIZE = 2;

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int capacity;
    private int size;
    private float threshold;
    private Node<K,V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = calculateThreshold();
        size = 0;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
        } else {
            putForNotNullKey(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return findValueByKey(0, null);
        }
        for (int i = 0; i < table.length; i++) {
            V value;
            if ((value = findValueByKey(i, key)) != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[0];
            while (currentNode.next != null && currentNode.key != null) {
                currentNode = currentNode.next;
            }
            if (currentNode.key == null) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(null, value, null);
                size++;
            }
        }
    }

    private void putForNotNullKey(K key, V value) {
        int index = indexFor(hash(key));
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null && !key.equals(currentNode.key)) {
                currentNode = currentNode.next;
            }
            if (key.equals(currentNode.key)) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(key, value, null);
                size++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        size = 0;
        capacity *= DEFAULT_GROW_SIZE;
        threshold = calculateThreshold();
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> tableItem : oldTable) {
            if (tableItem == null) {
                continue;
            }
            Node<K, V> currentNode = tableItem;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int indexFor(int h) {
        return (h & 0x7fffffff) % capacity;
    }

    private float calculateThreshold() {
        return capacity * DEFAULT_LOAD_FACTOR;
    }

    private V findValueByKey(int index, K key) {
        if (table[index] == null) {
            return null;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }
}
