package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 1 << 4;
    static final double LOAD_FACTOR = 0.75;
    static final int RESIZE_MULTIPLAYER = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (getSize() >= table.length * LOAD_FACTOR) {
            resize();
        }
        int indexOfBacked = getIndexOfBacked(key);
        Node<K, V> currentNode = table[indexOfBacked];
        if (currentNode == null) {
            table[indexOfBacked] = new Node<>(key, value, null);
        }
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key,value,null);
                break;
            }
            currentNode = currentNode.next;
        }
        size++;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length * RESIZE_MULTIPLAYER];
        for (Node<K, V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfBacked(K key) {
        return key == null ? 0 : Math.abs(Objects.hashCode(key)) % table.length;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }
}
