package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;  //16
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

    public MyHashMap() {

        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        Node<K, V> currentNode = table[getBucketNumber(key)];
        if (currentNode == null) {
            table[getBucketNumber(key)] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (true) {
                if (key == null && currentNode.key == null || Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash(key), key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucketNumber(key)];
        while (currentNode != null) {
            if (key == null && currentNode.key == null || Objects.equals(key, currentNode.key)) {
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

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        threshold = threshold << 1;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        table = newTable;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            if (currentNode != null) {
                if (currentNode.next == null) {
                    put(currentNode.key, currentNode.value);
                } else {
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private void transfer() {

    }

    private int getBucketNumber(K key) {
        if (hash(key) < 0) {
            return hash(key) % table.length * -1;
        }
        return hash(key) % table.length;
    }

    ///////////// DELETE PRINT ////////////////////
    public void print() {
        int position = 0;
        for (Node<K, V> node : table) {
            System.out.print(position + " ");
            if (node == null || node.next == null) {
            System.out.println(node);
            } else {
                System.out.print(node);
                System.out.println(" --> " + node.next);
            }
            position++;
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        ////////////// DELETE TO STRING /////////////
        @Override
        public String toString() {
            return "Node{" +
                    "hash=" + hash +
                    ", key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
