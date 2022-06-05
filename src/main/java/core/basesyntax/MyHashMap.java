package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    private void iterateNodes(Node<K,V> node, K key, V value) {
        boolean isNewNode = true;
        Node<K,V> prevNode = null;
        do {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                node.value = value;
                isNewNode = false;
                break;
            } else {
                prevNode = node;
                node = node.next;
            }
        } while (node != null);
        if (isNewNode) {
            prevNode.next = new Node<>(key, value, null);
            size++;
        }
    }
    private void resize() {

        if (size == threshold) {
            size = 0;
            int newCapacity = table.length << 1;
            threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
            Node<K,V>[] oldTable = table;
            table = new Node[newCapacity];
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    do {
                        put(node.key, node.value);
                        node = node.next;
                    } while (node != null);
                }
            }
        }
    }
    @Override
    public void put(K key, V value) {
        isEmpty();
        int keyHash = key == null ? 0 : key.hashCode();
        int index = Math.abs(keyHash % table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);;
            size++;
        } else {
            iterateNodes(table[index], key, value);
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        isEmpty();
        int index = key == null ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K,V> node = table[index];
        if (node != null) {
            do {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                } else {
                    node = node.next;
                }
            } while (node != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void isEmpty() {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = key == null ? 0 : key.hashCode();
        }
    }
}
