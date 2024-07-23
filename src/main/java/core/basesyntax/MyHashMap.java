package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_SIZE = 16;
    private static final double RESIZE_BOUND = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        threshold = (int) (table.length * RESIZE_BOUND);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = calculateIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node current = table[index];
            while (current != null) {
                if (key == current.key || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
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
        if (size >= threshold) {
            size = 0;
            Node<K, V>[] newArray = new Node[table.length * 2];
            Node<K, V>[] oldArray = table;
            table = newArray;
            threshold = (int) (table.length * RESIZE_BOUND);
            for (Node<K, V> node : oldArray) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int calculateIndex(K key) {
        return key == null ? 0 : key.hashCode() & (table.length - 1);
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
