package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] data;
    private int size;

    public MyHashMap() {
        data = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<K, V>(calculateHash(key), key, value, null);
        int index = getIndexByKey(key);

        Node<K, V> node = data[index];
        Node<K, V> prev = null;

        if (node == null) {
            data[index] = newNode;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }

                prev = node;
                node = node.next;
            }

            prev.next = newNode;
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> node = data[index];
        if (node != null) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
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

    private void resize() {

    }

    private int calculateHash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int getIndexByKey(K key) {
        return calculateHash(key) & (data.length - 1);
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
