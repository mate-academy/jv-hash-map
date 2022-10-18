package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
//    private final float loadFactor;

    public MyHashMap() {
//        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    static class Node<K, V> {
        final int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        table = resize();
        int hash = getHash(key);
        int index = Math.abs(hash) % table.length;
        Node<K, V> node = table[index];
        boolean isOverwritten = false;
        if (node == null) {
            node = new Node<>(hash, key, value, null);
            table[index] = node;
            size++;
        } else {
            while (true) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    node.value = value;
                    isOverwritten = true;
                    break;
                } else if (node.next != null) {
                    node = node.next;
                } else {
                    break;
                }
            }
            if (!isOverwritten) {
                node.next = new Node<>(hash, key, value, null);
                size ++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash = getHash(key);
        int index = Math.abs(hash) % table.length;
        Node<K, V> node = table[index];
        Node<K, V> equal = findEqual(node, key);
        if (equal != null) {
            return equal.value;
        }
        return null;
    }

    private Node<K, V> findEqual(Node<K, V> node, K key) {
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] newTab;
        if (table == null || table.length == 0) {
            threshold = (int) (INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            newTab = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        } else if (size > threshold) {
            newTab = transfer();
        } else {
            newTab = table;
        }
        return newTab;
    }

    private Node<K, V>[] transfer() {
        int newCap = table.length * 2;
        threshold = threshold * 2;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> nodeFrom = table[i];
            if (nodeFrom != null) {
                int index = Math.abs(nodeFrom.hash) % newCap;
                Node<K, V> nodeTo = newTab[index];
                if (nodeTo != null) {
                    getTail(nodeTo).next = nodeFrom;
                } else {
                    newTab[index] = nodeFrom;
                }
            }
        }
        return newTab;
    }

    private Node<K, V> getTail(Node<K, V> nodeTo) {
        while (nodeTo.next != null) {
            nodeTo = nodeTo.next;
        }
        return nodeTo;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }
}
