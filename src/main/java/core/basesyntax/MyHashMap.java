package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private int size = 0;
    private int threshold;

    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        boolean isNew = true;
        Node<K, V>[] tab = table;
        Node<K, V> nodeAtPosition;
        int tableLength;
        int pointer;
        if (tab == null || (tableLength = tab.length) == 0) {
            tableLength = (tab = resize()).length;
        }
        if ((nodeAtPosition = tab[pointer = (hash & (tableLength - 1))]) == null) {
            tab[pointer] = new Node<>(key, value, hash, null);
        } else {
            K exKey = nodeAtPosition.key;
            Node<K, V> existingMapping;
            if (nodeAtPosition.hash == hash
                    && (Objects.equals(key, exKey))) {
                existingMapping = nodeAtPosition;
            } else {
                for (int bucket = 0; ; ++bucket) {
                    if ((existingMapping = nodeAtPosition.next) == null) {
                        nodeAtPosition.next = new Node<>(key, value, hash, null);
                        break;
                    }
                    exKey = existingMapping.key;
                    if (existingMapping.hash == hash
                            && (Objects.equals(key, exKey))) {
                        break;
                    }
                    nodeAtPosition = existingMapping;
                }
            }
            if (existingMapping != null) {
                existingMapping.value = value;
                isNew = false;
            }
        }
        if (isNew) {
            if (++size > threshold) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int hash = hash(key);
        Node<K, V> currNode = table[hash & (table.length - 1)];
        while (currNode != null) {
            if (currNode.hash == hash
                    && (Objects.equals(key, currNode.key))) {
                return currNode.value;
            } else {
                currNode = currNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private Node<K, V>[] resize() {
        int oldCap = (table == null) ? 0 : table.length;
        int oldThr = threshold;
        int newCap;
        int newThr = 0;

        if (oldCap > 0) {
            newCap = oldCap << 1;
            if (oldThr > 0) {
                newThr = oldThr << 1;
            }
        } else {
            newCap = DEFAULT_CAPACITY;
            newThr = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        threshold = newThr;
        return transfering(oldCap, newCap);
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] transfering(int oldCap, int newCap) {
        Node<K, V>[] oldTab = table;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;

        for (int i = 0; i < oldCap; i++) {
            Node<K, V> node;
            if ((node = oldTab[i]) != null) {
                oldTab[i] = null;
                if (node.next == null) {
                    newTab[node.hash & (newCap - 1)] = node;
                } else {
                    Node<K, V> loHead = null;
                    Node<K, V> loTail = null;
                    Node<K, V> hiHead = null;
                    Node<K, V> hiTail = null;
                    do {
                        if ((node.hash & oldCap) == 0) {
                            if (loTail == null) {
                                loHead = node;
                            } else {
                                loTail.next = node;
                            }
                            loTail = node;
                        } else {
                            if (hiTail == null) {
                                hiHead = node;
                            } else {
                                hiTail.next = node;
                            }
                            hiTail = node;
                        }
                    } while ((node = node.next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[i] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[i + oldCap] = hiHead;
                    }
                }
            }
        }
        return newTab;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
