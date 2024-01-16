package core.basesyntax;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final static int DEFAULT_INITIAL_CAPACITY = 16;
    private final static double DEFAULT_LOAD_FACTORY = 0.75;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] testArray = new Node[]{};
    private List<Node<K, V>> myHashMap;

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        myHashMap = new ArrayList<>();
        fillNull(myHashMap);
        threshold = (int) (DEFAULT_LOAD_FACTORY * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        if (checkSize()) {
            resize();
        }
        if (myHashMap.get(node.hash) == null) {
            myHashMap.set(node.hash, node);
            size++;
        } else {
            Node oldNode = myHashMap.get(node.hash);
            while (oldNode.next != null) {
                if (oldNode.key == null && node.key == null
                        || oldNode.key != null && oldNode.key.equals(node.key)) {
                    oldNode.value = node.value;
                    return;
                }
                oldNode = oldNode.next;
            }
            if (oldNode.key == null && node.key == null
                    || oldNode.key != null && oldNode.key.equals(node.key)) {
                oldNode.value = node.value;
                return;
            }
            oldNode.next = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        if (myHashMap.get(hash(key)) == null) {
            return null;
        }
        Node<K, V> tempNode = myHashMap.get(hash(key));
        while (tempNode != null) {
            if (tempNode.key == null && key == null
                    || tempNode.key != null && tempNode.key.equals(key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void fillNull(List<Node<K, V>> targetList) {
        for (int i = 0; i < capacity; i++) {
            targetList.add(null);
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private boolean checkSize() {
        return size + 1 > threshold;
    }

    private void resize() {
        grow();
        List<Node<K, V>> oldMap = myHashMap;
        myHashMap = new ArrayList<>();
        fillNull(myHashMap);
        Node<K, V> tempNode;
        size = 0;
        for (int i = 0; i < oldMap.size(); i++) {
            tempNode = oldMap.get(i);
            if (tempNode != null) {
                if (tempNode != null) {
                    while (tempNode != null) {
                        put(tempNode.key, tempNode.value);
                        tempNode = tempNode.next;
                    }
                } else {
                    put(tempNode.key, tempNode.value);
                }
            }
        }
    }

    private void grow() {
        capacity *= 2;
        threshold = (int) (DEFAULT_LOAD_FACTORY * capacity);
    }
}

/*
Put
1) Add if null
2) Add if node
3) change if key equals
4) checkSize
5) resize
Get
6) getValue
*/