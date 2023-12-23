package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float GROW_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[0];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.hash = (key == null) ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        } else if (size >= (int)(table.length * GROW_FACTOR)) {
            Node<K, V> [] oldTable = table;
            resize();
        }
        int bucket = hash(key);
        if (table[bucket] == null) {
            table[bucket] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> node = table[bucket];
            while (node != null) {
                if (node.key == null && key == null) {
                    node.value = value;
                    break;
                }
                if (node.key != null && key != null) {
                    if ((node.key.hashCode() == key.hashCode()) && (node.key.equals(key))) {
                        node.value = value;
                        break;
                    }
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    break;
                } else {
                    node = node.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        int index = hash(key);
        Node<K, V> bucket = table[index];
        if (bucket.next == null) {
            return bucket.value;
        }
        while (bucket != null) {
            if (Objects.equals(bucket.key, key)) {
                return bucket.value;
            } else {
                bucket = bucket.next;
                if (bucket.next == null) {
                    return bucket.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode()) % ((int)(table.length * GROW_FACTOR)));
    }

    private void resize() {
        size = 0;
        if (table == null || table.length == 0) {
            table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        } else {
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[oldTable.length * 2];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    if (node.next == null) {
                        break;
                    }
                    node = node.next;
                }
            }
        }
    }
}
