package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOADFACTOR = 0.75f;
    private int size;
    private float loadFactor;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        loadFactor = DEFAULT_LOADFACTOR;
    }

    public MyHashMap(int capacity, float loadFactor) {
        table = new Node[capacity];
        this.loadFactor = loadFactor;
    }

    @Override
    public void put(K key, V value) {
        int hashPosition = hash(key) % table.length;
        if (size < getThreshold()) {
            if (table[hashPosition] == null) {
                table[hashPosition] = new Node<>(key, value, null);
                ++size;
            } else {
                Node<K, V> node = table[hashPosition];
                while (node != null) {
                    if (checkNodeForSameKey(node, key)) {
                        node.value = value;
                        return;
                    }
                    if (node.next == null) {
                        break;
                    } else {
                        node = node.next;
                    }
                }
                node.next = new Node<>(key, value, null);
                size++;
            }
        } else {
            resize();
            put(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key) % table.length;
        Node<K, V> node = table[hash];
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

    private boolean checkNodeForSameKey(Node<K, V> node, K key) {
        if (Objects.equals(node.key, key)) {
            return true;
        } else {
            return false;
        }
    }

    private void resize() {
        List<Node<K,V>> nodes = getAllNodes();
        table = new Node[table.length * 2];
        int tempSize = size;
        size = 0;
        for (int i = 0; i < tempSize; i++) {
            Node<K, V> tempNode = nodes.get(i);
            put(tempNode.key, tempNode.value);
        }
    }

    private List<Node<K, V>> getAllNodes() {
        List<Node<K,V>> nodes = new ArrayList<>(size);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> node = table[i];
                while (node != null) {
                    nodes.add(node);
                    node = node.next;
                }
            }
        }
        return nodes;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getThreshold() {
        return (int) (loadFactor * table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            hash = hash(key);

        }

        private int hash(K key) {
            return key == null ? 0 : key.hashCode();
        }
    }
}
