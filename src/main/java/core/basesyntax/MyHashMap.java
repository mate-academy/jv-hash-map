package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private static final int SIZE_INCREASE_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getValidIndex(hash(key, table.length), table.length);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = newNode;
        } else {
            do {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            } while (currentNode.next != null);
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = newNode;
        }
        if (++size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getValidIndex(hash(key, table.length), table.length);
        Node<K, V> node = getNode(index);
        if (index <= table.length && node != null) {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key, int capacity) {
        return (key == null) ? 0 : key.hashCode() % capacity;
    }

    private int getValidIndex(int hashKey, int capacity) {
        return (capacity - 1) & hashKey;
    }

    private Node<K,V> getNode(int index) {
        return table[index] != null ? table[index] : null;
    }

    private void resize() {
        int newCapacity = table.length * SIZE_INCREASE_FACTOR;
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        for (Node<K, V> node : table) {
            if (node != null) {
                Node<K, V> oldNode = node;
                while (oldNode != null) {
                    int index = getValidIndex(hash(oldNode.key, newCapacity), newCapacity);
                    Node<K, V> newNode = new Node<>(oldNode.key, oldNode.value, null);
                    Node<K, V> newNodePosition = newTable[index];
                    if (newNodePosition == null) {
                        newTable[index] = newNode;
                    } else {
                        while (newNodePosition.next != null) {
                            newNodePosition = newNodePosition.next;
                        }
                        newNodePosition.next = newNode;
                    }
                    oldNode = oldNode.next;
                }
            }
        }
        table = newTable;
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

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
