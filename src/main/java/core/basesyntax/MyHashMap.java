package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] map;

    public MyHashMap() {
        map = new Node[DEFAULT_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getNodeIndex(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (map[index] == null) {
            map[index] = newNode;
            size++;
        } else {
            Node<K, V> node = map[index];
            while (node != null) {
                if (Objects.equals(newNode.key, node.key)) {
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
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = map[getNodeIndex(key)];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getNodeIndex(K key) {
        return (key == null) ? 0 : Math.abs((key.hashCode() % map.length));
    }

    private void resizeIfNeeded() {
        if (size > map.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldMap = map;
            map = new Node[map.length * GROW_FACTOR];
            for (Node<K, V> node : oldMap) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
