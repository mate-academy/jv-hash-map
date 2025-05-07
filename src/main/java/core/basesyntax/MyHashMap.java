package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    private MyNode<K, V>[] myTable = new MyNode[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        MyNode<K, V> node = myTable[getIndexByKey(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new MyNode<>(key, value, null);
                size++;
                return;
            }
            node = node.next;
        }

        myTable[getIndexByKey(key)] = new MyNode<>(key, value, null);
        size++;
    }

    private void resize() {
        size = 0;
        int newSize = myTable.length * 2;
        threshold = DEFAULT_INITIAL_CAPACITY * newSize;
        MyNode<K, V>[] oldTable = myTable;
        myTable = new MyNode[newSize];
        for (MyNode<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        MyNode<K, V> node = myTable[getIndexByKey(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % myTable.length;
    }

    @Override
    public int getSize() {
        return size;
    }

    public class MyNode<K, V> {
        private K key;
        private V value;
        private MyNode<K, V> next;

        public MyNode(K key, V value, MyNode<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

