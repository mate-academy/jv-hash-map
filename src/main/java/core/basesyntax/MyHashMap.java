package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float THRESHOLD_COEFFICIENT = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;
    private Node<K, V> tail;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * THRESHOLD_COEFFICIENT);
        this.size = 0;
        this.tail = null;
    }

    @Override
    public void put(K key, V value) {
        int hash = getIndexFromKey(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[hash] == null) {
            size++;
            resizeIfNeeded();
            table[hash] = newNode;
        } else {
            Node<K, V> node = checkKeyIntoCell(key);

            if (node != null) {
                node.value = value;
            } else {
                size++;
                tail.next = newNode;
                tail = newNode;
                resizeIfNeeded();
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getIndexFromKey(key);
        if (table[hash] == null) {
            System.out.println("the key is absent in the map");
        } else {
            Node<K, V> node = checkKeyIntoCell(key);

            if (node != null) {
                return node.value;
            } else {
                throw new RuntimeException("the key is absent: " + key);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexFromKey(Object key) {
        return (key == null) ? 0 : Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        threshold = threshold << 1;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> tempNode;
            Node<K, V> nodeForSaveNext;
            int hash;
            if ((tempNode = oldTable[i]) != null) {
                oldTable[i] = null;
                while (tempNode != null) {
                    nodeForSaveNext = tempNode.next;
                    tempNode.next = null;
                    hash = getIndexFromKey(tempNode.key);
                    if (table[hash] == null) {
                        table[hash] = tempNode;
                    } else {
                        Node<K, V> next = table[hash];
                        while (next.next != null) {
                            next = next.next;
                        }
                        next.next = tempNode;
                    }
                    tempNode = nodeForSaveNext;
                }
            }
        }
    }

    private void resizeIfNeeded() {
        if (size > threshold) {
            resize();
        }
    }

    private boolean isSameKey(K key, K newKey) {
        return (getIndexFromKey(key) == getIndexFromKey(newKey) && key == newKey)
                || (key != null && key.equals(newKey));
    }

    private Node<K, V> checkKeyIntoCell(K key) {
        Node<K, V> node = table[getIndexFromKey(key)];
        Node<K, V> temp = null;

        while (node != null) {
            if (isSameKey(node.key, key)) {
                return node;
            } else {
                temp = node;
                node = node.next;
            }
        }
        tail = temp;
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
