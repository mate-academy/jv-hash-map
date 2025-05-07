package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFFAULT_CAPPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TRESHOLD_RESIZE_MULTIPLIER = 2;
    private static final int TABLE_RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int treshold;

    public MyHashMap() {
        treshold = (int) (DEFFAULT_CAPPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFFAULT_CAPPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int indexBucket = getbacketIndex(key);
        Node<K, V> thisNode = table[indexBucket];
        if (thisNode == null) {
            table[indexBucket] = new Node<>(key, value);
            size++;
            return;
        }
        while (thisNode.next != null || Objects.equals(thisNode.key, key)) {
            if (Objects.equals(thisNode.key, key)) {
                thisNode.value = value;
                return;
            }
            thisNode = thisNode.next;
        }
        thisNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> thisNode = table[getbacketIndex(key)];
        while (thisNode != null) {
            if (Objects.equals(thisNode.key, key)) {
                return thisNode.value;
            }
            thisNode = thisNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int capacity;
        if (size >= treshold) {
            capacity = table.length * TABLE_RESIZE_MULTIPLIER;
            treshold = treshold * TRESHOLD_RESIZE_MULTIPLIER;
            table = new Node[capacity];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getbacketIndex(Object k) {
        return k == null ? 0 : Math.abs(k.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}

