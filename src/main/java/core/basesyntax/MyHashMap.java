package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAGNIFICATION_TABLE = 2;

    private Node<K,V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        createTableOrExpend();
        int index = getIndexByKeyHash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key,value);
            size++;
        } else if (Objects.equals(table[index].key,key)) {
            table[index].value = value;
        } else {
            putNodeForeTail(key,value);
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int index = getIndexByKeyHash(key);
            if (Objects.equals(table[index].key,key)) {
                return table[index].value;
            }
            Node<K, V> tail = table[index].next;
            while (tail != null) {
                if (Objects.equals(tail.key,key)) {
                    return tail.value;
                }
                tail = tail.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNodeForeTail(K key, V value) {
        Node<K,V> afterHead = table[getIndexByKeyHash(key)];
        while (afterHead.next != null) {
            if (Objects.equals(afterHead.key,key)) {
                afterHead.value = value;
                return;
            }
            afterHead = afterHead.next;
        }
        if (Objects.equals(afterHead.key,key)) {
            afterHead.value = value;
            return;
        }
        afterHead.next = new Node<>(key,value);
        size++;
    }

    private void createTableOrExpend() {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
        } else if (size == threshold) {
            Node<K,V>[] oldTable = table;
            table = new Node[table.length * MAGNIFICATION_TABLE];
            for (Node<K,V> node: oldTable) {
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                    node.next = null;
                    if (table[getIndexByKeyHash(node.key)] == null) {
                        table[getIndexByKeyHash(node.key)] = node;
                    } else {
                        Node<K,V> tail = table[getIndexByKeyHash(node.key)];
                        while (tail.next != null) {
                            tail = tail.next;
                        }
                        tail.next = node;
                    }
                    node = nextNode;
                }
            }
        }
        threshold = (int)(table.length * DEFAULT_LOAD_FACTOR);
    }

    private int getIndexByKeyHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
