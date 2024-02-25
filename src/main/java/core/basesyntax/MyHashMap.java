package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Node<K, V>[] nodeArray = new Node[DEFAULT_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> noda = nodeArray[getIndex(key)];
        if (noda == null) {
            Node<K, V> currentNode = new Node<>(key, value, null);
            nodeArray[getIndex(key)] = currentNode;
            size++;
        } else {
            while (noda.next != null || Objects.equals(noda.key, key)) {
                if (Objects.equals(noda.key, key)) {
                    noda.value = value;
                    return;
                }
                noda = noda.next;
            }
            noda.next = new Node<>(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> noda = nodeArray[getIndex(key)];
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
        return Math.abs(key.hashCode()) % nodeArray.length;
    }

    private void resize() {
        if (size > nodeArray.length * LOAD_FACTOR) {
            int newSize = nodeArray.length * 2;
            Node<K, V>[] newNodeArray = new Node[newSize];
            Node<K, V>[] oldNodeArray = nodeArray;
            nodeArray = newNodeArray;
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

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
