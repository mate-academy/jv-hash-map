package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MOD_FACTOR = 2;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    MyHashMap() {
        table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > table.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value);
        int placeInArray = positionByHash(key);

        if (table[placeInArray] == null) {
            table[placeInArray] = node;
        } else {
            Node<K, V> thatNode = table[placeInArray];
            while (true) {
                if (Objects.equals(thatNode.key, key)) {
                    thatNode.value = value;
                    return;
                }
                if (thatNode.next == null) {
                    break;
                }
                thatNode = thatNode.next;
            }
            thatNode.next = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = positionByHash(key);
        if (position < 0 || position > table.length) {
            return null;
        }
        Node<K, V> node = table[position];
        V value = null;
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    @SuppressWarnings("unchecked")
    private void resize() {
        size = 0;
        int newCapacity = table.length * MOD_FACTOR;
        Node<K, V>[] oldArray = table;
        table = (Node<K,V>[]) new Node[newCapacity];
        replaceAll(oldArray);
    }

    private void replaceAll(Node<K, V>[] fromArray) {
        for (Node<K, V> kvNode : fromArray) {
            if (kvNode != null) {
                Node<K, V> node = kvNode;

                while (node != null) {
                    Node<K, V> tempNode = node.next;
                    node.next = null;
                    put(node.key, node.value);
                    node = tempNode;
                }
            }
        }
    }

    private int positionByHash(K key) {
        return Math.abs(hash(key)) % table.length;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + "=" + value;
        }
    }
}
