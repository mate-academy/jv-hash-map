package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putNode(key,value,true);
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = findIndexBucket(hash(key));
        if (table[index] == null) {
            return null;
        }
        Node<K,V> findNode = table[index];
        while (findNode != null) {
            if (Objects.equals(key,findNode.key)) {
                break;
            }
            findNode = findNode.next;
        }
        return (findNode == null) ? null : findNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Boolean replaceValueInBucket(int index, K key, V value) {
        Node<K,V> findNode = table[index];
        while (findNode != null) {
            if (key == findNode.key
                    || key != null
                    && key.equals(findNode.key)
                    && key.hashCode() == findNode.key.hashCode()) {
                findNode.value = value;
                return true;
            }
            findNode = findNode.next;
        }
        return false;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int findIndexBucket(int hash) {
        return hash & (table.length - 1);
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * 2];
            threshold *= 2;
            for (Node<K, V> oldNode : oldTable) {
                if (oldNode != null) {
                    Node<K,V> findNode = oldNode;
                    while (findNode != null) {
                        putNode(findNode.key,findNode.value,false);
                        findNode = findNode.next;
                    }
                }
            }
        }
    }

    private void putNode(K key, V value, Boolean increaseSize) {
        int hash = hash(key);
        int index = findIndexBucket(hash);
        Node<K,V> addNode = new Node<>(hash,key,value,null);
        if (table[index] == null) {
            table[index] = addNode;
            size = (increaseSize) ? (size + 1) : size;
        }
        Boolean replace = replaceValueInBucket(index,key,value);
        if (!replace) {
            Node<K,V> findNode = table[index];
            while (findNode.next != null) {
                findNode = findNode.next;
            }
            findNode.next = addNode;
            size = (increaseSize) ? (size + 1) : size;
        }
    }

    private class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
