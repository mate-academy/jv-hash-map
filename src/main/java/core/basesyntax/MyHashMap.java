package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkThreshhold();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int newCap;
        newCap = table.length * 2;
        Node<K, V>[] newTable = new Node[newCap];
        transfer(table, newTable);
        table = newTable;
    }

    private void checkThreshhold() {
        int threshHold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size > threshHold) {
            resize();
        }
    }

    private void transfer(Node<K, V>[] table, Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = node.key == null ? 0 :
                        Math.abs(node.key.hashCode()) % newTable.length;
                Node<K, V> newNode = new Node<>(node.key, node.value);
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = newNode;
                } else {
                    Node<K, V> currentNode = newTable[newIndex];
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                    }
                    currentNode.next = newNode;
                }
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
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
            return Objects.equals(key, node.key)
                   && Objects.equals(value, node.value)
                   && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + ((key == null) ? 0 : key.hashCode());
            result = 31 * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
