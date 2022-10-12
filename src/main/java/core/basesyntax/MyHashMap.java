package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private final float loadFactor = 0.75f;
    private final int firstIndex = 0;
    private int initialSize = 16;
    private Node<K, V>[] table;
    private int size = 0;

    MyHashMap() {
        table = new Node[initialSize];
        for (int i = 0; i < initialSize; i++) {
            table[i] = null;
        }
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private int hash;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= initialSize * loadFactor) {
            checkSize();
        }
        Node<K, V> first;
        int index;
        if (key == null) {
            if (table[firstIndex] == null) {
                table[firstIndex] = new Node<>(0, null, value, null);
                size++;
                return;
            }
            first = table[firstIndex];
            if (first.key == null) {
                first.value = value;
                return;
            }
            while (first.next != null) {
                first = first.next;
                if (first.key == null) {
                    first.value = value;
                    return;
                }
            }
            first.next = new Node<>(0, null, value, null);
            size++;
            return;
        }
        index = Math.abs(key.hashCode()) % initialSize;
        first = table[index];
        if (table[index] == null) {
            first = new Node<>(key.hashCode(), key, value, null);
            table[index] = first;
            size++;
            return;
        }
        if (table[index] != null) {
            while (first != null) {
                if (key.equals(first.key)) {
                    first.value = value;
                    return;
                }
                first = first.next;
            }
        }
        first = table[index];
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node<>(key.hashCode(), key, value, null);
        size++;
    }

    private void checkSize() {
        initialSize *= 2;
        size = 0;
        Node<K, V>[] tempO = table;
        table = new Node[initialSize];
        for (Node<K, V> element : tempO) {
            if (element != null) {
                Node<K, V> first = element;
                do {
                    put(first.key, first.value);
                    first = first.next;
                } while (first != null);
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> first;
        if (key == null) {
            first = table[firstIndex];
            while (first != null) {
                if (first.key == null) {
                    return first.value;
                }
                first = first.next;
            }
            return null;
        }
        int index = Math.abs(key.hashCode()) % initialSize;
        if (table[index] == null) {
            return null;
        } else {
            first = table[index];
            while (first != null) {
                if ((key.hashCode() == (first.hash)) && key.equals(first.key)) {
                    return first.value;
                }
                first = first.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}

