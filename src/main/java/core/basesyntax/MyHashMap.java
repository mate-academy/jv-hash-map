package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_NUMBER = 2;
    private int size;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hashKey(key);
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = hashKey(key);
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private float getThreshold() {
        float threshold = hashTable.length * LOAD_FACTOR;
        return threshold;
    }

    private void resize() {
        if (size >= getThreshold()) {
            size = 0;
            Node<K, V>[] oldTable = hashTable;
            hashTable = new Node[oldTable.length * INCREASE_NUMBER];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hashKey(Object key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return Math.floorMod(hash, hashTable.length);
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> newNode = new Node<>(key, value, hashTable[index]);
        hashTable[index] = newNode;
        size++;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
