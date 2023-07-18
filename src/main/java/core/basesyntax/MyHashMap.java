package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MODIFICATOR = 2;
    private Node<K,V>[] table;
    private int size;
    private int accessibleLoadness;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        accessibleLoadness = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> nodeToFind = findNodeByKey(key);
        if (nodeToFind == null) {
            addNode(key, value);
        } else {
            nodeToFind.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> nodeToFind = findNodeByKey(key);
        return nodeToFind == null ? null : nodeToFind.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * RESIZE_MODIFICATOR;
        accessibleLoadness = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            for (; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private Node<K, V> findNodeByKey(K key) {
        int hash = getHashByKey(key);
        int index = getIndexByHash(hash);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
        }
        return null;
    }

    private void addNode(K key, V value) {
        int hash = getHashByKey(key);
        int indexOfBucket = getIndexByHash(hash);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[indexOfBucket] != null) {
            Node<K, V> lastNodeOfBucket = getLastNode(table[indexOfBucket]);
            lastNodeOfBucket.next = newNode;
        } else {
            table[indexOfBucket] = newNode;
        }
        if (++size > accessibleLoadness) {
            resize();
        }
    }

    private Node<K, V> getLastNode(Node<K,V> head) {
        for (Node<K, V> node = head; ; node = node.next) {
            if (node.next == null) {
                return node;
            }
        }
    }

    private int getHashByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndexByHash(int hash) {
        return hash % table.length;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
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
    }
}
