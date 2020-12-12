package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTORY = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int CAPACITY_AND_THRESHOLD_MULTIPLIER = 2;
    private final int capacity;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        size = 0;
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = (Node<K,V>[])new Node[capacity];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTORY);
    }

    @Override
    public void put(K key, V value) {
        int indexForValue = indexInTable(key);
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table[indexForValue] == null) {
            table[indexForValue] = newNode;
            size++;
        } else {
            isUniqueKey(newNode, table);
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int indexWantedValue = indexInTable(key);
        if (table[indexWantedValue] == null) {
            return null;
        }
        if (Objects.equals(table[indexWantedValue].key, key)) {
            return table[indexWantedValue].value;
        } else {
            Node<K, V> wantedNode = table[indexWantedValue];
            while (wantedNode != null) {
                if (isEquals(wantedNode.key, key)) {
                    return wantedNode.value;
                }
                wantedNode = wantedNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        int hash;
        K key;
        V value;
        Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        int newThreshold = threshold * CAPACITY_AND_THRESHOLD_MULTIPLIER;
        int newCapacity = capacity * CAPACITY_AND_THRESHOLD_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        threshold = newThreshold;
        int indexInNewTable;
        for (Node<K, V> currentNode : oldTable) {
            if (currentNode != null) {
                indexInNewTable = indexInTable(currentNode.key);
                if (table[indexInNewTable] == null) {
                    table[indexInNewTable] = currentNode;
                } else {
                    isUniqueKey(currentNode, table);
                }
            }
        }
    }

    private void isUniqueKey(Node<K, V> current, Node<K, V>[] table) {
        int indexForValue = indexInTable(current.key);
        Node<K, V> checkedNode = table[indexForValue];
        if (isEquals(current.key, checkedNode.key)) {
            checkedNode.value = current.value;
        } else {
            while (checkedNode.next != null) {
                if (isEquals(current.key, checkedNode.key)) {
                    checkedNode.value = current.value;
                    return;
                }
                checkedNode = checkedNode.next;
            }
            if (isEquals(current.key, checkedNode.key)) {
                checkedNode.value = current.value;
                return;
            }
            checkedNode.next = current;
            size++;
        }
    }

    private boolean isEquals(K key, K secondKey) {
        return key == null && secondKey == null || Objects.equals(key, secondKey);
    }

    private int hash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode());
    }

    private int indexInTable(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % table.length);
    }
}
