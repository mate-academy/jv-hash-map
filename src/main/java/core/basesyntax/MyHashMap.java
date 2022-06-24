package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INCREASE_CAPACITY_STEP = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private int size;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        grow();
        putValue(hashCode(key),key,value);
    }

    @Override
    public V getValue(K key) {
        int position = countPosition(hashCode(key));
        if (table[position] == null) {
            return null;
        }
        Node<K,V> node = table[position];
        do {
            if (Objects.equals(node.key,key)) {
                return node.value;
            }
            node = node.next;
        } while (node != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void putValue(int hash,K key,V value) {
        int position = countPosition(hash);
        if (table[position] == null) {
            Node<K, V> newNode = new Node<>(hash,key,value,null);
            table[position] = newNode;
        } else {
            Node<K, V> node = table[position];
            while (node != null) {
                if (Objects.equals(node.key,key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
                    break;
                }
                node = node.next;
            }
        }
        size++;
    }

    private int countPosition(int hash) {
        return hash % table.length;
    }

    private int hashCode(K key) {
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        if (hash < 0) {
            return hash * -1;
        }
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void grow() {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K,V>[] oldTable = table;
            table = new Node[oldTable.length * INCREASE_CAPACITY_STEP];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    putValue(node.hash, node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
