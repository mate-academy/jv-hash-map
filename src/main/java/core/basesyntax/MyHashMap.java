package core.basesyntax;

import static java.lang.Math.abs;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] table;
    private int threshold;
    private int capacity;
    private float loadFactor;

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K,V> currentNode;
        Node<K,V> prev;
        if (table == null || table.length == 0) {
            resize();
        }
        int position = hash % table.length;
        K currKey;
        if ((currentNode = table[position]) == null) {
            table[position] = new Node<>(hash, key, value, null);
        } else {
            do {
                if (currentNode.hash == hash && ((currKey = currentNode.key) == key
                        || (key != null && key.equals(currKey)))) {
                    currentNode.value = value;
                    return;
                }
                prev = currentNode;
            } while ((currentNode = currentNode.next) != null);
            prev.next = new Node<>(hash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K,V> currentNode;
        K keyCurrent;
        if (table != null && table.length > 0
           && (currentNode = table[hash % table.length]) != null) {
            do {
                if (currentNode.hash == hash && ((keyCurrent = currentNode.key) == key
                    || (key != null && key.equals(keyCurrent)))) {
                    return currentNode.value;
                }
            } while ((currentNode = currentNode.next) != null);
            }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()));
    }

    private void resize() {
        if (capacity == 0) {
            capacity = DEFAULT_INITIAL_CAPACITY;
            loadFactor = DEFAULT_LOAD_FACTOR;
            threshold = (int)(capacity * loadFactor);
        } else if (capacity > 0) {
            if (capacity >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
            } else {
                threshold = threshold << 1;
                capacity = capacity << 1;
            }
        }
        Node<K, V>[] newTable = (Node<K,V>[])new Node[capacity];
        if (table != null) {
            transfer(newTable);
        } else {
            table = newTable;
        }
    }

    private void transfer(Node<K, V>[] newTable) {
        int newCap = newTable.length;
        Node<K, V> currNode;
        int position;
        for (Node<K, V> node : table) {
            while (node != null) {
                position = node.hash % newCap;
                currNode = newTable[position];
                if (newTable[position] == null) {
                    newTable[position] = new Node<K, V>(node.hash, node.key, node.value, null);
                } else {
                    while (currNode.next != null) {
                        currNode = currNode.next;
                    }
                    currNode.next = new Node<K, V>(node.hash, node.key, node.value, null);;
                }
                node = node.next;
            }
        }
        table = newTable;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
