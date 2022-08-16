package core.basesyntax;

import static java.util.Objects.hash;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASING_RATE = 2;
    private Node<K, V>[] hashTable;
    private int size;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndexOfKey(key);
        Node<K, V> node = hashTable[index];
        if (node == null) {
            hashTable[index] = new Node(key, value, null);
        } else {
            while (true) {
                K nodeKey = node.key;
                if (key == nodeKey || (key != null && key.equals(nodeKey))) {
                    node.value = value;
                    size--;
                    break;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    break;
                }
                node = node.next;
            }
        }
        if (++size > hashTable.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexOfKey(key);
        Node<K, V> node = hashTable[index];
        while (node != null) {
            if (key == node.key
                    || (key != null && key.equals(node.key))) {
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

    private void resize() {
        Node<K, V>[] oldTable = hashTable;
        hashTable = new Node[hashTable.length * INCREASING_RATE];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndexOfKey(K key) {
        int hashCode = hash(key);
        return Math.abs(hashCode) % hashTable.length;
    }

    private static class Node<K, V> {
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
