package core.basesyntax;

import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTORY = 0.75;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] myHashMap;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        myHashMap = new Node[capacity];
        //fillNull(myHashMap);
        threshold = (int) (DEFAULT_LOAD_FACTORY * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        if (checkSize()) {
            resize();
        }
        if (myHashMap[node.hash] == null) {
            myHashMap[node.hash] = node;
            size++;
        } else {
            Node oldNode = myHashMap[node.hash];
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
        if (myHashMap[hash(key)] == null) {
            return null;
        }
        Node<K, V> tempNode = myHashMap[hash(key)];
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
        Node<K, V>[] oldMap = myHashMap;
        myHashMap = new Node[capacity];
        Node<K, V> tempNode;
        size = 0;
        for (int i = 0; i < oldMap.length; i++) {
            tempNode = oldMap[i];
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
