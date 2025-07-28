package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int MY_DEFAULT_CAPACITY = 1 << 4; //16
    static final float MY_LOAD_FACTOR = 0.75f;
    private int size;

    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[MY_DEFAULT_CAPACITY];
        size = 0;
    }

    static class Node<K, V> {

        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    final int getIndex(K key) {
        return (key == null) ? 0 : Math.abs((key.hashCode() % table.length));
    }

    @Override
    public void put(K key, V value) {
        resize();
        putVal(key, value);

    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void resize() {

        if (size < table.length * MY_LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1]; //oldTable * 2
        int oldSize = size;
        size = 0;

        for (Node<K, V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    putVal(node.key, node.value);
                    node = node.next;
                }
            }
        }
        size = oldSize;
    }

    private void putVal(K key, V value) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newCurrent = new Node<>(key, value);

        if (current == null) {
            table[index] = newCurrent;
            size++;
            return;
        }

        while (current != null) {
            if (Objects.equals(current.key, newCurrent.key)) {
                current.value = value;
                return;
            }

            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = newCurrent;
        size++;
    }
}
