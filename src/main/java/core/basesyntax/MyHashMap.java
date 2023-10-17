package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final Integer KEY_NULL_VALUE = 0;
    private static final Integer BIT_SHIFT_BY_ONE = 1;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growIfSizeIsInLoadFactory();
        int index = Math.abs(key == null ? 0 : key.hashCode()) % table.length;
        Node newNode = new Node<>(key, value, null);
        Node current = table[index];
        while (current != null) {
            if (current.key != null && current.key.equals(key)
                    || current.key == null && key == null) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node current = table[i];
                while (current != null) {
                    if (current.key != null && current.key.equals(key)
                            || current.key == null && key == null) {
                        return (V) current.value;
                    }
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void growIfSizeIsInLoadFactory() {
        if (size >= table.length * LOAD_FACTOR) {
            Node[] tmpArrNode = table;
            table = new Node[table.length << BIT_SHIFT_BY_ONE];
            size = 0;
            for (int i = 0; i < tmpArrNode.length; i++) {
                Node current = tmpArrNode[i];
                while (current != null) {
                    put((K) current.key, (V) current.value);
                    current = current.next;
                }
            }
        }
    }

    private class Node<K, V> {
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
