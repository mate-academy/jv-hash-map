package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        threshold = (int) (DEFAULT_SIZE * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        grow();
        int buketIndex = getBuketIndex(getHash(key));
        if (table[buketIndex] == null) {
            table[buketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> curNode = table[buketIndex];
            while (curNode != null) {
                if (Objects.equals(key, curNode.key)) {
                    curNode.value = value;
                    return;
                }
                if (curNode.next == null) {
                    curNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                curNode = curNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> curNode = table[getHash(key) % table.length];
        while (curNode != null) {
            if (Objects.equals(key, curNode.key)) {
                return curNode.value;
            }
            curNode = curNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        int code;
        return Math.abs(key == null ? 0 : (code = key.hashCode()) ^ (code >>> 16));
    }

    private void grow() {
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length << 1];
            threshold = threshold << 1;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getBuketIndex(int hash) {
        return hash % table.length;
    }

    static class Node<K, V> {
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
