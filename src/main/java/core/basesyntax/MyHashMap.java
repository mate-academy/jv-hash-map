package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float THRESHOLD_COEFFICIENT = 0.75f;
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * THRESHOLD_COEFFICIENT);
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int size;

    public MyHashMap() {
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() % capacity;
    }

    private Node<K, V>[] resize() {
        capacity = capacity << 1;
        threshold = threshold << 1;
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> tempNode;
            int hash;
            if ((tempNode = oldTable[i]) != null) {
                oldTable[i] = null;
                newTable[hash(tempNode.key)] = tempNode;
                if (tempNode.next != null) {
                    Node<K, V> tempNodeNext = tempNode.next;
                    tempNode.next = null;
                    do {
                        hash = hash(tempNodeNext.key);
                        if (newTable[hash] == null) {
                            newTable[hash] = tempNodeNext;
                            newTable[hash].next = null;
                            tempNodeNext = tempNodeNext.next;
                        }else {
                            Node<K, V> nodeTail = newTable[hash];
                            while (nodeTail.next != null) {
                                nodeTail = nodeTail.next;
                            }
                            nodeTail.next = tempNode;
                        }
                    }while (tempNodeNext != null);
                }
            }
        }
        return newTable;
    }

    private void checkOnResize() {
        if (size > threshold) {
            table = resize();
        }
    }

    private boolean isSameKey(K key, K newKey) {
        return (hash(key) == hash(newKey) && key == newKey)
                || (key != null && key.equals(newKey));
    }

    private Node<K, V> checkKeyIntoCell(int keyHash, K key, Node<K, V> node) {
        boolean isKey = false;
        isKey = isSameKey(node.key, key);

        while(!isKey) {
            if (node.next != null) {
                node = node.next;
                isKey = isSameKey(node.key, key);
            } else {
                return node;
            }
        }
        return node;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K, V> newNode = new Node<>(key, value, hash, null);;

        if (table[hash] == null) {
            size++;
            checkOnResize();
            table[hash] = newNode;
        } else {
            Node<K, V> node = table[hash];
            node = checkKeyIntoCell(hash, key, node);

//            if (isKey) {
//                node.value = value;
//            }else {
//                size++;
//                node.next = newNode;
//            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            System.out.println("the key is absent in the map");
        } else {
            Node<K, V> node = table[hash];
            node = checkKeyIntoCell(hash, key, node);
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        public int hashCode() {
            return Objects.hashCode(this.key);
        }

    }
}
