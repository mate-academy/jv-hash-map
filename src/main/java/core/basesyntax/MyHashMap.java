package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] map;

    public MyHashMap() {
        map = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * map.length) {
            resize();
        }
        int index = generateIndex(key);
        if (map[index] == null) {
            map[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K,V> tempNode = findInsertNode(map[index], key);
        if (!Objects.equals(key, tempNode.key)) {
            tempNode.next = new Node<>(key, value, null);
            size++;
            return;
        }
        tempNode.value = value;
    }

    @Override
    public V getValue(K key) {
        int index = generateIndex(key);
        Node<K,V> tempNode = map[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int generateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % map.length;
    }

    private Node<K,V> findInsertNode(Node<K,V> node, K key) {
        while (node.next != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return node;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldMap = map;
        map = new Node[map.length << 1];
        for (Node<K,V> node: oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private V value;
        private K key;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.value = value;
            this.key = key;
        }
    }
}
