package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int bucket = hash % table.length;
        Node<K,V> newNode = new Node<>(hash, key, value, null);
        Node<K, V> checkNode;

        // check collision
        checkNode = table[bucket];

        if (checkNode == null) {
            table[bucket] = newNode;
            size++;
        } else {
            do {
                if (compareSolution(checkNode, key)) {
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
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = hash(key) % table.length;
        Node<K, V> checkNode;

        checkNode = table[bucket];

        if (checkNode != null) {
            if (compareSolution(checkNode, key)) {
                return checkNode.value;
            }

            if (checkNode.next != null) {
                checkNode = checkNode.next;
                while (checkNode != null) {
                    if (compareSolution(checkNode, key)) {
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
        threshold = (int) (newCap * DEFAULT_LOAD_FACTOR);
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

    private boolean compareSolution(Node<K,V> object, K key) {
        return Objects.equals(object.key, key) || object.key != null && object.key.equals(key);
    }

    private static int hash(Object key) {
        int hash = 17;
        return (key == null) ? 0 : Math.abs((hash = hash * key.hashCode()) ^ (hash >>> 16));
    }

    private static class Node<K,V> {
        private int hash;
        private Node<K,V> next;
        private final K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
