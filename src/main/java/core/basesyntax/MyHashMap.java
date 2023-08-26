package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private final int defaultCapacity = 16; // aka 16
    private final float loadFactor = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[defaultCapacity];
        threshold = (int) (table.length * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = calculateIndex(key);
        //if node is null, creating new node
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                // if we found same key, updating the value
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
            }
            // if no same key found, adding node to head
            Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
            table[index] = newNode;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        if (table[index] != null) {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int calculateIndex(K key) {
        return hash(key) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length << 1];
        size = 0;
        threshold = (int) (table.length * loadFactor);
        for (Node<K, V> nodes : oldTable) {
            for (Node<K, V> node = nodes; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private int hash;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
