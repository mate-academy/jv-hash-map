package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            Node<K, V> current = table[0];
            while (current != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            table[0] = new Node<>(null, value, table[0]);
            size++;
            if (size > capacity * LOAD_FACTOR) {
                resize();
            }
            return;
        }

        int index = getIndex(key, capacity);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            if (size > capacity * LOAD_FACTOR) {
                resize();
            }
        } else {
            Node<K, V> current = table[index];
            while (true) {
                if (key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value, null);
                    size++;
                    if (size > capacity * LOAD_FACTOR) {
                        resize();
                    }
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> current = table[0];
            while (current != null) {
                if (current.key == null) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        int index = getIndex(key, capacity);
        Node<K, V> current = table[index];
        while (current != null) {
            if (key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                int newIndex;
                Node<K, V> next = node.next;

                if (node.key == null) {
                    newIndex = 0;
                } else {
                    newIndex = Math.abs(node.key.hashCode() % newCapacity);
                }

                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
    }

    private int getIndex(K key, int modCapacity) {
        return Math.abs(key.hashCode() % modCapacity);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
