package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }

        int bucket = getBucketNumber(key);
        Node<K,V> newNode = new Node<>(key, value, null);
        Node<K, V> checkNode;

        // check collision
        checkNode = table[bucket];

        if (checkNode == null) {
            table[bucket] = newNode;
            size++;
        } else {
            do {
                if (Objects.equals(checkNode.key, key)) {
                    checkNode.value = value;
                    break;
                }

                if (checkNode.next == null) {
                    checkNode.next = newNode;
                    size++;
                    break;
                } else {
                    checkNode = checkNode.next;
                }
            } while (true);
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucketNumber(key) % table.length;
        Node<K, V> checkNode;

        checkNode = table[bucket];

        if (checkNode != null) {
            if (Objects.equals(checkNode.key, key)) {
                return checkNode.value;
            }

            if (checkNode.next != null) {
                checkNode = checkNode.next;
                while (checkNode != null) {
                    if (Objects.equals(checkNode.key, key)) {
                        return checkNode.value;
                    }
                    checkNode = checkNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int oldCap = table.length;
        int newCap = oldCap << 1;
        size = 0;

        Node<K, V> checkNode;
        Node<K, V>[] oldTab = table;
        table = new Node[newCap];

        for (Node<K, V> node : oldTab) {
            if (node != null) {
                put(node.key, node.value);

                if (node.next != null) {
                    checkNode = node.next;
                    do {
                        put(checkNode.key, checkNode.value);
                        checkNode = checkNode.next;
                    } while (checkNode != null);
                }
            }
        }
    }

    private int getBucketNumber(Object key) {
        int hash = 17;
        hash = (key == null) ? 0 : Math.abs((hash = hash * key.hashCode()) ^ (hash >>> 16));
        return hash % table.length;
    }

    private static class Node<K,V> {
        private Node<K,V> next;
        private final K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
