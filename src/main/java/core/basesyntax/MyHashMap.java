package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = calculateHash(key);
        int index = calculateIndex(hash);
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value);
            size++;
        } else {
            Node<K, V> currNode = table[index];
            while (currNode != null) {
                if (currNode.hash == hash && Objects.equals(currNode.key, key)) {
                    currNode.value = value;
                    break;
                }
                if (currNode.next == null) {
                    currNode.next = new Node<>(hash, key, value);
                    size++;
                    break;
                }
                currNode = currNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = calculateHash(key);
        int index = calculateIndex(hash);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> currNode = table[index];
        do {
            if (currNode.hash == hash && Objects.equals(currNode.key, key)) {
                return currNode.value;
            }
        } while ((currNode = currNode.next) != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length << 1;
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int index = node.hash % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = nextNode;
            }
        }
        table = newTable;
    }

    private int calculateHash(K key) {
        int keyHashCode;
        return key == null ? 0
                : ((keyHashCode = key.hashCode()) ^ (keyHashCode >>> 16)) & 0xfffffff;
    }

    private int calculateIndex(int hash) {
        return hash & (table.length - 1);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
