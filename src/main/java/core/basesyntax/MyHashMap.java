package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_RATE = 2;
    private Node<K, V>[] tables;
    private int dynamicSize;
    private int threshold;
    private int size;

    public MyHashMap() {
        tables = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * tables.length);
    }

    @Override
    public void put(K key, V value) {
        dynamicSize = tables.length;
        Node<K, V> newNode;
        int index;
        if (size > threshold) {
            resize();
        }
        Objects.checkIndex(index = index(key), tables.length);
        if (key == null) {
            key = (K) Integer.valueOf(index);
        }
        if (tables[index] == null) {
            newNode = new Node<>(index, key, value, null);
            tables[index] = newNode;
            size++;
            return;
        }
        newNode = new Node<>(index, key, value, null);
        if (index == tables[index].index) {
            Node<K, V> tmpTables = tables[index];
            while (tmpTables != null) {
                if (newNode.key.equals(tmpTables.key)) {
                    tmpTables.setValue(newNode.value);
                    return;
                }
                tmpTables = tmpTables.next;
            }
        }
        newNode.next = tables[index].next;
        tables[index].next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        dynamicSize = tables.length;
        if (key == null) {
            key = (K) Integer.valueOf(0);
        }
        int index = index(key);
        Node<K, V> tmpNode;
        for (tmpNode = tables[index]; tmpNode != null; tmpNode = tmpNode.next) {
            if (tmpNode.key.equals(key)) {
                return tmpNode.value;
            }

        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = tables;
        Node<K, V>[] newTable = new Node[oldTable.length * INCREASE_RATE];
        dynamicSize = newTable.length;
        int index;

        for (int i = 0; i < oldTable.length; ++i) {
            Node<K, V> tmp;
            if ((tmp = oldTable[i]) != null) {
                oldTable[i] = null;
                if (tmp.next == null) {
                    index = index(tmp.key);
                    newTable[index] = tmp;
                }
                Node<K, V> current = tmp;
                while (current != null) {
                    index = index(current.key);
                    newTable[index] = tmp;
                    current = current.next;
                }
            }
        }
        threshold = (int) (newTable.length * LOAD_FACTOR);
        return tables = newTable;
    }

    private int index(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % dynamicSize);
    }

    private static class Node<K, V> {
        private final K key;
        private final int index;
        private Node<K, V> next;
        private V value;

        public Node(int index, K key, V value, Node<K, V> next) {
            this.index = index;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return index == node.index && key.equals(node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, key, value, next);
        }
    }
}
