package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexForBucket(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            ++size;
        return;
        }
        putInFullBucket(index, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> value;
        return (value = getNode(key)) == null ? null : value.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexForBucket(K key) {
        return Math.abs(Objects.hashCode(key)) % table.length;
    }

    private void putInFullBucket(int index, K key, V value) {
        Node<K, V> currentNode = table[index];
        if ((key == null && currentNode.key == null)
                || (key != null && key.equals(currentNode.key))) {
            currentNode.value = value;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if ((key == null && currentNode.key == null)
                        || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = new Node<>(key, value, null);
            ++size;
        }
    }

    private void resize() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldTable = table;
            int oldCapacity = oldTable.length;
            threshold = threshold << 1;
            table = new Node[oldCapacity << 1];
                for (Node<K, V> node : oldTable) {
                    if (node != null) {
                        put(node.key, node.value);
                        while (node.next != null) {
                            node = node.next;
                            put(node.key, node.value);
                        }
                    }
                }
            }
        }

    private Node<K, V> getNode(K key) {
            Node<K, V> node = table[getIndexForBucket(key)];
                if (node != null) {
                    if ((key == null && node.key == null)
                            || (key != null && key.equals(node.key))) {
                        return node;
                    }
                    while (node.next != null) {
                        node = node.next;
                        if ((key == null && node.key == null)
                                || (key != null && key.equals(node.key))) {
                            return node;
                        }
                    }
                }
                return null;
            }


    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
