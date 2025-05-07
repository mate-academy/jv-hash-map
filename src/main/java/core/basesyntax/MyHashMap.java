package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getIndex(key);
        resize();
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value, null);
        } else {
            for (Node<K, V> node = table[bucketIndex]; node != null; node = node.next) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    break;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
        Node<K,V> node;
        node = table[bucketIndex];
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

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(K key) {
        return getHash(key) % table.length;
    }

    private void resize() {
        if (size == (table.length * LOAD_FACTOR)) {
            Node<K,V>[] oldTable = table;
            size = 0;
            table = new Node[table.length * MAGNIFICATION_FACTOR];
            for (Node<K,V> transferingNode : oldTable) {
                while (transferingNode != null) {
                    put(transferingNode.key,transferingNode.value);
                    transferingNode = transferingNode.next;
                }
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
        }
    }
}
