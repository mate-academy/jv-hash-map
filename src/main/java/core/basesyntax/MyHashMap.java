package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private void checkThreshold() {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_MULTIPLIER];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = key == null ? 0 : key.hashCode();
        int index = hash & (table.length - 1);

        Node<K, V> current = table[index];
        while (current != null) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.hash = hash;
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        checkThreshold();
    }

    @Override
    public V getValue(K key) {
        int hash = key == null ? 0 : key.hashCode();
        int index = hash & (table.length - 1);

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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
