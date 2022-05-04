package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        putValue(key, value, true);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(hash(key))];
        if (currentNode != null) {
            do {
                if (checkKey(key, currentNode)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getIndex(int hash) {
        return hash & (table.length - 1);
    }

    private void resize() {
        if (table.length < MAX_CAPACITY) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            for (Node<K, V> oldNode : oldTable) {
                while (oldNode != null) {
                    putValue(oldNode.key, oldNode.value, false);
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private boolean checkKey(K key, Node<K, V> node) {
        return hash(key) == node.hash && Objects.equals(key, node.key);
    }

    private void putValue(K key, V value, boolean isCheckSizeAndKey) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = getIndex(hash(key));
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = newNode;
        } else {
            do {
                if (isCheckSizeAndKey) {
                    if (checkKey(key, currentNode)) {
                        currentNode.value = value;
                        return;
                    }
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        if (isCheckSizeAndKey) {
            if (++size > table.length * LOAD_FACTOR) {
                resize();
            }
        }
    }
}
