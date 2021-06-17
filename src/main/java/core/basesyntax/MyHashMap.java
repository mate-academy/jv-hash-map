package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int treshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        treshold = (int) (LOAD_FACTOR * INITIAL_CAPACITY);
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (treshold == size) {
            resize();
        }
        int indexToPut = indexFor(key);
        Node<K, V> node = new Node<>(key, value, null);
        size++;
        if (table[indexToPut] != null) {
            Node<K, V> nodeRunner = table[indexToPut];
            while (nodeRunner != null) {
                if (rewriteNode(nodeRunner, key, value)) {
                    return;
                }
                if (nodeRunner.next == null) {
                    nodeRunner.next = node;
                    break;
                }
                nodeRunner = nodeRunner.next;
            }
            return;
        }
        table[indexToPut] = node;
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        Node<K, V> node = table[index];
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        treshold *= (int) (LOAD_FACTOR * table.length);
        table = new Node[table.length << 1];
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int indexFor(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private boolean rewriteNode(Node<K, V> node, K key, V value) {
        if (Objects.equals(node.key, key)) {
            node.value = value;
            size--;
            return true;
        }
        return false;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
