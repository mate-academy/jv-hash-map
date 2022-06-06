package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = findIndex(key);
        Node<K, V> node = new Node<K, V>(key, value, null);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> nodeCurrent = table[index];
            while (nodeCurrent != null) {
                if (Objects.equals(nodeCurrent.key, key)) {
                    nodeCurrent.value = value;
                    break;
                }
                if (nodeCurrent.next == null) {
                    nodeCurrent.next = node;
                    size++;
                    break;
                }
                nodeCurrent = nodeCurrent.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[findIndex(key)];
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

    private int findIndex(K key) {
        return (key == null) ? 0 : (key.hashCode() >>> 16) % table.length;
    }

    private void resize() {
        threshold *= 2;
        size = 0;
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length * 2];
        copyTab(oldTab);
    }

    private void copyTab(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
