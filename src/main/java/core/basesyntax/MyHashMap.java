package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int position = getBucketIndex(key);
        Node<K, V> currentNode = table[position];
        if (currentNode == null) {
            table[position] = new Node(key, value, null);
            size++;
            return;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
        table[position] = new Node(key, value, table[position]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = getBucketIndex(key);
        Node<K, V> nodeByKey = table[position];
        while (nodeByKey != null) {
            if (Objects.equals(nodeByKey.key, key)) {
                return nodeByKey.value;
            }
            nodeByKey = nodeByKey.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if (size > LOAD_FACTOR * table.length) {
            Node<K, V>[] oldTable = table;
            int newCapacity = table.length << 1;
            table = new Node[newCapacity];
            for (Node<K, V> oldNode : oldTable) {
                while (oldNode != null) {
                    put(oldNode.key, oldNode.value);
                    oldNode = oldNode.next;
                    size--;
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
