package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DEFAULT_COEFFICIENT = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizer();
        int bucket = getIndex(key);
        Node<K,V> node = table[bucket];
        Node<K,V> newNode = new Node<>(key, value, null);
        if (node == null) {
            table[bucket] = newNode;
        }
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = newNode;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = getIndex(key);
        Node<K,V> node = table[bucket];
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

    private void resizer() {
        if (threshold == size) {
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * DEFAULT_COEFFICIENT];
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getHashcode(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode()) ^ (key.hashCode() >>> 16); /*We need this impl due to possibility
        of numbers of bucket collisions if we use default hash method*/
    }

    private int getIndex(K key) {
        int index = getHashcode(key) % table.length;
        if (index < 0) {
            index -= index;
        }
        return index;
    }

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
}
