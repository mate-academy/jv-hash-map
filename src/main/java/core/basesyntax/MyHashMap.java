package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= this.table.length * LOAD_FACTOR) {
            resize();
        }
        int index = calculateIndex(key, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if ((key == null && current.key == null)
                        || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value);
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[calculateIndex(key, table.length)];
        while (current != null) {
            if ((key == null && current.key == null)
                    || key != null && key.equals(current.key)) {
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
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * GROW_FACTOR];
        size = 0;
        for (Node<K, V> current : oldTable) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int calculateIndex(Object key, int tableCapacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % tableCapacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
