package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float THRESHOLD_COEFFICIENT = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V> tail;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * THRESHOLD_COEFFICIENT);
        capacity = INITIAL_CAPACITY;
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[hash] == null) {
            size++;
            resizeCheck();
            table[hash] = newNode;
        } else {
            Node<K, V> node = checkKey(key);
            if (node != null) {
                node.value = value;
            } else {
                size++;
                tail.next = newNode;
                tail = newNode;
                resizeCheck();
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            System.out.println("the key is absent in the map");
        } else {
            Node<K, V> node = checkKey(key);

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

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(Objects.hashCode(key)) % capacity;
    }

    private Node<K, V>[] resize() {
        capacity = capacity << 1;
        threshold = threshold << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> tempNode;
            int hash;
            if ((tempNode = table[i]) != null) {
                table[i] = null;

                while (tempNode != null) {
                    Node<K, V> nodeForSaveNext = tempNode.next;
                    hash = hash(tempNode.key);
                    if (newTable[hash] == null) {
                        newTable[hash] = tempNode;
                        newTable[hash].next = null;
                    } else {
                        Node<K, V> next = newTable[hash];
                        while (next.next != null) {
                            next = next.next;
                        }
                        next.next = tempNode;
                        next.next.next = null;
                    }
                    tempNode = nodeForSaveNext;
                }
            }
        }
        return newTable;
    }

    private void resizeCheck() {
        if (size > threshold) {
            table = resize();
        }
    }

    private boolean isSameKey(K key, K newKey) {
        return (hash(key) == hash(newKey) && key == newKey) || (key != null && key.equals(newKey));
    }

    private Node<K, V> checkKey(K key) {
        Node<K, V> node = table[hash(key)];
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
