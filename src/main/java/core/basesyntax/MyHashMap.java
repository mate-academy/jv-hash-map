package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int initialSize = 16;
    private int size = 0;

    MyHashMap() {
        table = new Node[initialSize];
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
        Node<K, V> first;
        int index;
        if (size >= initialSize * LOAD_FACTOR) {
            resize();
        }
        index = (key == null ? 0 : Math.abs(key.hashCode())) % initialSize;
        first = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key == null ? 0 : key.hashCode(), key, value, null);
            size++;
            return;
        }
        if (table[index] != null) {
            while (first != null) {
                if ((first.key == null && key == null) || (key != null && key.equals(first.key))) {
                    first.value = value;
                    return;
                }
                first = first.next;
            }
        }
        setNewNode(index, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> first;
        int index = (key == null ? 0 : Math.abs(key.hashCode())) % initialSize;
        if (table[index] == null) {
            return null;
        } else {
            first = table[index];
            while (first != null) {
                if (first.key == null && key == null) {
                    return first.value;
                } else if (key != null && key.equals(first.key)) {
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

    private void resize() {
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

    private void setNewNode(int index, K key, V value) {
        Node<K, V> first;
        first = table[index];
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node<>(key == null ? 0 : key.hashCode(), key, value, null);
        size++;
    }
}
