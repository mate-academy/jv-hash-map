package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (checkSize(size)) {
            resize();
        }
        if (putVal(key, value, table, getIndex(key, table.length))) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key, table.length)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    private boolean putVal(K key, V value, Node<K, V>[] nodes, int index) {
        if (nodes[index] == null) {
            nodes[index] = new Node<>(hashCode(key), key, value);
            return true;
        } else {
            Node<K, V> currentNode = table[index];
            while (!Objects.equals(currentNode.key, key)) {
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(hashCode(key), key, value);
                    return true;
                }
                currentNode = currentNode.next;
            }
            currentNode.value = value;
        }
        return false;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCap = table.length;
        int oldThr = threshold;
        int newCap = oldCap << 1;
        threshold = oldThr << 1;

        Node<K, V>[] newNodes = new Node[newCap];
        table = newNodes;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putVal(node.key, node.value, newNodes, getIndex(node.key, newNodes.length));
                node = node.next;
            }
        }
        table = newNodes;
    }

    public boolean checkSize(int size) {
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        return size >= threshold;
    }

    private int getIndex(K key, int capacity) {
        return hashCode(key) % capacity;
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }
}
