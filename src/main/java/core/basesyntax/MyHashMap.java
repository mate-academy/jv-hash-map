package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & (table.length - 1));
    }

    @Override
    public void put(K key, V value) {
        if (key == null && value == null) {
            throw new IllegalArgumentException("Key and value cannot both be null");
        }

        int index = hash(key);
        Node<K, V> existingNode = table[index];

        if (existingNode == null) {
            table[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> prev = null;
            Node<K, V> current = existingNode;
            while (current != null) {
                if (current.key == null && key == null
                        || (current.key != null && current.key.equals(key))) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = new Node<>(key, value, null);
        }

        size++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == null && key == null || (node.key != null && node.key.equals(key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public void remove(K key) {
        int index = hash(key);
        Node<K, V> node = table[index];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.key == null && key == null || (node.key != null && node.key.equals(key))) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return;
            }
            prev = node;
            node = node.next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> head : oldTable) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return getValue(key) != null;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
