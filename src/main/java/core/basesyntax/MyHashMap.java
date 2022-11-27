package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private final float loadFactor = 0.75f;
    private Node<K, V>[] tables;
    private int threshold;
    private int size = 0;

    public MyHashMap() {

        tables = new Node[defaultCapacity];
        threshold = (int) (loadFactor * tables.length);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = null;
        int index = index(key);
        Objects.checkIndex(index, tables.length);
        if (key == null) {
            key = (K) Integer.valueOf(index);
        }

        if (tables[index] == null) {
            newNode = new Node<>(index, key, value, null);
            tables[index(key)] = newNode;
            if (size++ > threshold) {
                resize();
            }
            return;
        }
        newNode = new Node<>(index, key, value, null);
        if (newNode.getHash() == tables[index].hash) {
            Node<K, V> tmpTables = tables[index];
            while (tmpTables != null) {
                if (newNode.key.equals(tmpTables.key)) {
                    tmpTables.setValue(newNode.getValue());
                    return;
                }
                tmpTables = tmpTables.next;
            }
        }
        newNode.next = tables[index].next;
        tables[index].next = newNode;
        if (size++ > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            key = (K) Integer.valueOf(0);
        }
        int index = index(key);
        if (index < 0) {
            index = index * (-1);
        }
        Node<K, V> tmpNode;
        for (tmpNode = tables[index]; tmpNode != null; tmpNode = tmpNode.next) {
            if (tmpNode.getKey().equals(key)) {
                return tmpNode.getValue();
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
        Node<K, V>[] newTable = new Node[oldTable.length * 2];

        int index;
        for (int i = 0; i < oldTable.length; ++i) {
            Node<K, V> tmp;
            if ((tmp = oldTable[i]) != null) {
                oldTable[i] = null;
                if (tmp.next == null) {
                    index = tmp.key.hashCode() % newTable.length;
                    if (index < 0) {
                        index = index * (-1);
                    }
                    newTable[index] = tmp;
                }

                for (Node<K, V> current = tmp; current != null; current = current.next) {
                    index = current.key.hashCode() % newTable.length;
                    if (index < 0) {
                        index = index * (-1);
                    }
                    newTable[index] = tmp;

                }
            }
        }

        threshold = (int) (newTable.length * loadFactor);
        return tables = newTable;
    }

    private int index(K key) {
        int h = (key == null) ? 0 : (key.hashCode() % tables.length);
        if (h < 0) {
            return h * (-1);
        }
        return h;
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private Node<K, V> next;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
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
            return hash == node.hash && key.equals(node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
