package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int NULL_INDEX = 0;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(getHash(key), key, value, null);
        int index = getIndex(key);
        if (index == NULL_INDEX && table[NULL_INDEX] == null) {
            table[0] = newNode;
            size++;
        } else if (index == NULL_INDEX && table[NULL_INDEX] != null) {
            for (Node<K, V> node = table[NULL_INDEX]; node != null; node = node.next) {
                if (Objects.equals(key,node.key)) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
            }
        } else if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(key,node.key)) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
            }
        }
    }

    private int getHash(K key) {
        return Math.abs(Objects.hash(key));
    }

    private int getIndex(K key) {
        return Math.abs(Objects.hash(key) % table.length);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] tableUp = new Node[table.length * 2];
        for (Node<K, V> node : table) {
            for (Node<K, V> nodeCopy = node; nodeCopy != null; nodeCopy = nodeCopy.next) {
                Node<K, V> newNode = new Node<>(getHash(nodeCopy.key),
                        nodeCopy.key, nodeCopy.value, null);
                Node<K, V> temporaryNode = tableUp[Math.abs(nodeCopy.hash % tableUp.length)];
                if (temporaryNode == null) {
                    tableUp[Math.abs(nodeCopy.hash % tableUp.length)] = newNode;
                    continue;
                }
                while (temporaryNode.next != null) {
                    temporaryNode = temporaryNode.next;
                }
                temporaryNode.next = newNode;
            }
        }
        table = tableUp;
    }
}
