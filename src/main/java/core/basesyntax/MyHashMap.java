package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;

    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = buckets[getIndex(key)];
        if (node == null) {
            Node<K, V> currentNode = new Node<>(key, value, null);
            buckets[getIndex(key)] = currentNode;
            size++;
            return;
        }
        while (node.next != null || Objects.equals(node.key, key)) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        node.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> noda = buckets[getIndex(key)];
        if (noda == null) {
            return null;
        }
        while (!Objects.equals(noda.key, key)) {
            noda = noda.next;
        }
        return noda.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % buckets.length;
    }

    private void resize() {
        if (size > buckets.length * LOAD_FACTOR) {
            int newSize = buckets.length * GROW_FACTOR;
            Node<K, V>[] newNodeArray = new Node[newSize];
            Node<K, V>[] oldNodeArray = buckets;
            buckets = newNodeArray;
            size = 0;
            for (int i = 0; i < oldNodeArray.length; i++) {
                Node<K, V> kvNode = oldNodeArray[i];
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
