package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] map;

    public MyHashMap() {
        map = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = getHash(key);
        int index = getIndex(hash);
        Node<K, V> node = map[index];
        if (node == null) {
            map[index] = new Node<>(key, value, null);
            size++;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(getHash(key));
        Node<K, V> node = map[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    public int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() * map.length);
    }

    private int getIndex(int keyHash) {
        return keyHash % map.length;
    }

    private void resize() {
        size = 0;
        threshold = threshold * RESIZE_FACTOR;
        Node<K, V>[] temp = map;
        map = new Node[temp.length * RESIZE_FACTOR];
        for (int i = 0; i < temp.length; i++) {
            Node<K, V> node = temp[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
