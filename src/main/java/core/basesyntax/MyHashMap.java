package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) Math.round(DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }
        putVal(key, value);
    }

    @Override
    public V getValue(K key) {
        if (key == null && table[0] != null) {
            return readZeroPosition(key);
        }

        int currentIndex = getIndexFromKey(key);

        if (table[currentIndex] == null) {
            return null;
        }
        if (table[currentIndex].getKey() != key) {
            return readCollisionBucket(key, currentIndex);
        }
        return table[currentIndex].getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    private V readCollisionBucket(K key, int currentIndex) {
        Node<K, V> localCurrent = table[currentIndex];
        while (localCurrent != null) {
            if (Objects.equals(localCurrent.getKey(), key)) {
                return localCurrent.getValue();
            }
            localCurrent = localCurrent.getNext();
        }
        return null;
    }

    private V readZeroPosition(K key) {
        if (table[0].getKey() == null) {
            return table[0].getValue();
        }
        if (table[0].getNext() != null) {
            Node<K, V> current = table[0].getNext();
            while (current.getNext() != null) {
                if (current.getKey() == null) {
                    return current.getValue();
                }
                current = current.getNext();
            }
        }
        return null;
    }

    private void putVal(K key, V value) {
        if (key == null || key.hashCode() % table.length == 0) {
            zeroIndexPutHandler(key, value);
            return;
        }
        int currentIndex = getIndexFromKey(key);
        if (table[currentIndex] != null) {
            nonEmptyBucketPutHandler(key, value, currentIndex);
            return;
        }
        table[currentIndex] = new Node<K, V>(key, value, null);
        size++;
    }

    private void zeroIndexPutHandler(K key, V value) {
        Node<K, V> localCurrent = table[0];
        if (localCurrent == null) {
            table[0] = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        if (localCurrent.getKey() == key) {
            localCurrent.setValue(value);
            return;
        }
        while (localCurrent.getNext() != null) {
            if (localCurrent.getKey() == key) {
                localCurrent.setValue(value);
                return;
            }
            localCurrent = localCurrent.getNext();
        }
        localCurrent.setNext(new Node<K, V>(key, value, null));
        size++;
    }

    private void nonEmptyBucketPutHandler(K key, V value, int currentIndex) {
        if (table[currentIndex] != null) {
            if (Objects.equals(table[currentIndex].getKey(), key)) {
                table[currentIndex].setValue(value);
                return;
            }
            Node<K, V> localCurrent = table[currentIndex];
            while (localCurrent.getNext() != null) {
                localCurrent = localCurrent.getNext();
                if (Objects.equals(localCurrent.getKey(), key)) {
                    localCurrent.setValue(value);
                    return;
                }
            }
            localCurrent.setNext(new Node<K, V>(key, value, null));
            size++;
        }
    }

    private int getIndexFromKey(K key) {
        int currentIndex = key.hashCode() % table.length;
        currentIndex = currentIndex < 0 ? -1 * currentIndex : currentIndex;
        return currentIndex;
    }

    private void resize() {
        size = 0;
        threshold = threshold << 1;
        Node<K, V>[] temp = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        for (Node<K, V> kvNode : temp) {
            if (kvNode != null) {
                putVal(kvNode.getKey(), kvNode.getValue());
                while (kvNode.getNext() != null) {
                    kvNode = kvNode.getNext();
                    put(kvNode.getKey(), kvNode.getValue());
                }
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }
}
