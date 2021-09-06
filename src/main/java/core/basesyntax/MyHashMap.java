package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final int MULTIPLIER = 2;
    private Node<K,V> [] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = nodeIndex(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key,value,null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = new Node<>(key,value,null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = nodeIndex(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key,key)) {
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

    private int nodeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size == threshold) {
            threshold *= MULTIPLIER;
            Node<K,V>[] oldTable = table;
            table = new Node[table.length * MULTIPLIER];
            size = 0;
            for (Node<K,V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
