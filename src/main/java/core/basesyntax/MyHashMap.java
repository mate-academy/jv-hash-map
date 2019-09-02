package core.basesyntax;

import java.util.Arrays;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

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

        public Node<K, V> getNext() {
            return next;
        }

        public void setHash(int hash) {
            this.hash = hash;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacyty;
    private int size;
    private Node<K, V>[] table;
    private boolean isSizeChange = false;

    public MyHashMap() {
        capacyty = DEFAULT_CAPACITY;
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
    }

    public int getCapacyty() {
        return capacyty;
    }

    public Node<K, V>[] getTable() {
        return table;
    }

    public boolean isSizeChange() {
        return isSizeChange;
    }

    public void setCapacyty(int capacyty) {
        this.capacyty = capacyty;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTable(Node<K, V>[] table) {
        this.table = table;
    }

    public void setSizeChange(boolean sizeChange) {
        isSizeChange = sizeChange;
    }

    @Override
    public void put(K key, V value) {
        resize();

        if (key == null && table[0] == null) {
            table[0] = new Node<K, V>(0, null, value, null);
            size++;
            return;
        }
        if (key == null && table[0] != null) {
            table[0].setValue(value);
            return;
        }

        int index = index(hashCode(), capacyty);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<K, V>(key.hashCode(), key, value, current);
            size++;
            return;
        }
        while (current != null) {
            if (key == current.getKey() || current.getKey().equals(key)) {
                current.setValue(value);
                return;
            }
            current = current.getNext();
        }
        current = table[index];
        table[index] = new Node<K, V>(key.hashCode(), key, value, current);
        size++;
        return;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].getValue();
        }
        int hash = key.hashCode();
        int index = index(hashCode(), capacyty);
        Node<K, V> current = table[index];
        if (current == null) {
            return null;
        }
        Node<K, V> next = current.getNext();
        if (next == null) {
            return current.getValue();
        }
        while (current != null) {
            if (key == current.getKey() || key.equals(current.getKey())) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int index(int hash, int capacyty) {
        return hash % capacyty;
    }

    private void resize() {
        if (size > LOAD_FACTOR * capacyty) {
            setSize(0);
            int newCapacyty = capacyty * 3 / 2;
            setCapacyty(newCapacyty);
            Node<K, V>[] oldTable = Arrays.copyOf(table, table.length);
            setTable(new Node[capacyty]);
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    K k = oldTable[i].getKey();
                    V v = oldTable[i].getValue();
                    put(k, v);
                    Node<K, V> node = oldTable[i].getNext();
                    while (node != null) {
                        k = node.getKey();
                        v = node.getValue();
                        put(k, v);
                        node = node.getNext();
                    }
                }
            }
        }
    }
}
