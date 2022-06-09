package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int bucket = hash(key);
        Node<K, V> newNode = new Node(key, value, null);
        if (table[bucket] == null) {
            table[bucket] = newNode;
        } else {
            Node<K, V> currentNode = table[bucket];
            Node<K, V> previousNode = null;
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            if (previousNode != null) {
                previousNode.next = newNode;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int bucket = hash(key);
        Node<K, V> node = table[bucket];
        while (node != null) {
            if (node.key != null && node.key.equals(key) || node.key == key) {
                value = node.value;
                break;
            }
            node = node.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] newNodesArray = table;
            table = new Node[table.length << 1];
            for (Node<K, V> node : newNodesArray) {
                while (node != null) {
                    this.put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
