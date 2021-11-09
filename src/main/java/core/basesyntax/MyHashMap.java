package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CAPASITY = 16;
    private static final double HASHMAP_LOAD_FACTOR = 0.75;
    private Node<K, V>[] currentMap;
    private int size;

    public MyHashMap() {
        currentMap = new Node[START_CAPASITY];
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {

        Node<K, V> newNode = new Node(key, value, null);
        System.out.println(key + " " + value);
        int index = key == null ? 0 : Math.abs(key.hashCode()) % currentMap.length;
        System.out.println(index);
        Node<K, V>[] myOldMap = currentMap;
        if (currentMap[index] == null) {
            currentMap[index] = newNode;
            size++;
            return;
        }
        if (size == currentMap.length * HASHMAP_LOAD_FACTOR) {
            size = 0;
            int length = currentMap.length * 2;
            currentMap = new Node[length];
            for (Node<K, V> node : myOldMap) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
        Node<K, V> node = currentMap[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = currentMap[key == null ? 0 : Math.abs(key.hashCode())
                % currentMap.length];
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
}
