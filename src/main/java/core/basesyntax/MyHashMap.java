package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(getHash(key), key, value, null);
        } else {
            for (Node<K, V> x = currentNode; x != null; x = x.next) {
                if (Objects.equals(key, x.key)) {
                    x.value = value;
                    return;
                }
                if (x.next == null) {
                    x.next = new Node<>(getHash(key), key, value, null);
                    break;
                }
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode;
        if (table != null && table.length > 0 && (currentNode = table[getIndex(key)]) != null) {
            for (Node<K, V> x = currentNode; x != null; x = x.next) {
                if (Objects.equals(x.key, key)) {
                    return x.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
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
    }

    private Integer getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCap = oldTable == null ? 0 : oldTable.length;
        if (oldCap == 0) {
            table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (table.length * LOAD_FACTOR);
        }
        if (oldCap > 0) {
            int newCap = oldCap << 1;
            threshold = threshold << 1;
            size = 0;
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCap];
            table = newTable;
            transfer(oldTable, oldCap);
        }
    }

    private void transfer(Node<K, V>[] oldTable, int oldCap) {
        for (int i = 0; i < oldCap; i++) {
            Node<K, V> oldCurrentNode = oldTable[i];
            if (oldCurrentNode != null) {
                for (Node<K, V> first = oldCurrentNode; first != null; first = first.next) {
                    put(first.key, first.value);
                }
            }
        }
    }
}
