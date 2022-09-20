package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INITIAL_TABLE_SIZE = 1 << 4;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[INITIAL_TABLE_SIZE];
        threshold = (int) (INITIAL_TABLE_SIZE * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putElement(key, value, hashCode(key));
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int tableIndex = getTableIndex(key);
        if (table[tableIndex] == null) {
            return null;
        }
        Node<K, V> element = table[tableIndex];
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return "Node[ " + key + "=" + value + "]";
        }
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void putElement(K key, V value, int hash) {
        int tableIndex = getTableIndex(key);
        Node<K, V> element = table[tableIndex];
        if (element == null) {
            table[tableIndex] = new Node<>(hash, key, value, null);
        } else {
            while (element != null) {
                if (Objects.equals(element.key, key)) {
                    element.value = value;
                    return;
                }
                if (element.next == null) {
                    element.next = new Node<>(hash, key, value, null);
                    break;
                }
                element = element.next;
            }
        }
        size++;
    }

    private void resize() {
        int oldCapacity = table.length;
        threshold = threshold << 1;
        int newCapacity = oldCapacity << 1;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {
                Node<K, V> elem = oldTable[i];
                while (elem != null) {
                    putElement(elem.key, elem.value, elem.hash);
                    elem = elem.next;
                }
            }
        }
    }

    private int getTableIndex(K key) {
        return hashCode(key) % table.length;
    }
}
