package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_THRESHOLD = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    private static final int INCREASE_MULTIPLIER = 2;

    private Node<K, V>[] map;
    private int size;
    private int threshold;

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

    public MyHashMap() {
        map = new Node[INITIAL_CAPACITY];
        threshold = INITIAL_THRESHOLD;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (map[index] == null) {
            map[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> finalNode = map[index];
            while (finalNode.next != null || Objects.equals(finalNode.key, key)) {
                if (Objects.equals(finalNode.key, key)) {
                    finalNode.value = value;
                    return;
                }
                finalNode = finalNode.next;
            }
            finalNode.next = new Node<>(key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> searchNode = map[index];
        while (searchNode != null) {
            if (Objects.equals(searchNode.key, key)) {
                return searchNode.value;
            }
            searchNode = searchNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % map.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldMap = map;
        map = new Node[oldMap.length * INCREASE_MULTIPLIER];
        threshold = (int) (map.length * LOAD_FACTOR);
        transit(oldMap);
    }

    private void transit(Node<K, V>[] oldMap) {
        for (Node<K, V> node : oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
