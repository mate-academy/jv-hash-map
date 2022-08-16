package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final int DEFAULT_ARRAY_EXPANSION = 2;
    private Node<K, V>[] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        putNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int hash = getIndex(key);
        Node<K, V> curNode = table[hash];
        if (curNode != null) {
            while (curNode.next != null) {
                if (Objects.equals(curNode.key, key)) {
                    return curNode.value;
                }
                curNode = curNode.next;
            }
            return curNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        capacity = capacity * DEFAULT_ARRAY_EXPANSION;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        copy(oldTable);
    }

    private void copy(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> curNode : oldTable) {
            while (curNode != null) {
                putNode(curNode.key, curNode.value);
                curNode = curNode.next;
            }
        }
    }

    private void putNode(K key, V value) {
        int bucketIndex = getIndex(key);
        if (table[bucketIndex] == null) {
            table[bucketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> curNode = table[bucketIndex];
            if (Objects.equals(curNode.key, key)) {
                curNode.value = value;
                return;
            } else {
                while (curNode.next != null) {
                    if (Objects.equals(curNode.key, key)) {
                        break;
                    }
                    curNode = curNode.next;
                }
                if (Objects.equals(curNode.key, key)) {
                    curNode.value = value;
                    return;
                } else {
                    curNode.next = new Node<>(key, value, null);
                    size++;
                }
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
