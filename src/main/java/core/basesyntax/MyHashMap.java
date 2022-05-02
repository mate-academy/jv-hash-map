package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table == null) {
            table = new Node[INITIAL_CAPACITY];
        }
        int i = index(hash(key));
        Node<K, V> currentNode = table[i];
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (currentNode == null) {
            table[i] = newNode;
            if (++size > threshold) {
                resize();
            }
            return;
        }
        do {
            if (keyCheck(key, currentNode)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                if (++size > threshold) {
                    resize();
                }
                return;
            }
            currentNode = currentNode.next;
        } while (true);
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            for (Node<K, V> currentNode : table) {
                if (currentNode != null) {
                    do {
                        if (keyCheck(key, currentNode)) {
                            return currentNode.value;
                        }
                        currentNode = currentNode.next;
                    } while (currentNode != null);
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

    private int index(int hash) {
        return hash & (table.length - 1);
    }

    private void resize() {
        if (table.length < MAX_CAPACITY) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            for (Node<K, V> oldNode : oldTable) {
                while (oldNode != null) {
                    int newIndex = index(oldNode.hash);
                    Node<K, V> newNode = table[newIndex];
                    if (newNode == null) {
                        table[newIndex] = new Node<>(
                                oldNode.hash, oldNode.key, oldNode.value, null);
                    } else {
                        do {
                            if (newNode.next == null) {
                                newNode.next = new Node<>(
                                        oldNode.hash, oldNode.key, oldNode.value, null);
                                break;
                            }
                            newNode = newNode.next;
                        } while (true);
                    }
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private boolean keyCheck(K key, Node<K, V> node) {
        return hash(key) == node.hash && Objects.equals(key, node.key);
    }
}
