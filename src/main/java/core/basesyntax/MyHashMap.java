package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key == current.key
                    || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        table[index] = new Node<>(key, value, table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key == current.key
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resizeIfNeeded() {
        if (size >= table.length * LOAD_FACTOR) {
            int newCapacity = table.length * 2;
            Node<K, V>[] newTable = new Node[newCapacity];

            for (Node<K, V> node : table) {
                while (node != null) {
                    Node<K, V> next = node.next;
                    int index = node.key == null ? 0
                            : Math.abs(node.key.hashCode() % newCapacity);
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = next;
                }
            }
            table = newTable;
        }
    }
}
