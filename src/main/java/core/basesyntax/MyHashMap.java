package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> current = table[index];

        if (current == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }

        while (true) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }

            if (current.next == null) {
                break;
            }
            current = current.next;
        }

        current.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    private void resize() {
        capacity *= RESIZE_MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
