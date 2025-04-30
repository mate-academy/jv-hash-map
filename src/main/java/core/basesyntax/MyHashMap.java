package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int NEW_CAPACITY_MULTIPLIER = 2;
    private static final float LOAD_FACTOR = 0.75f;

    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int newCapacity = table.length * NEW_CAPACITY_MULTIPLIER;
        Node[] newTable = new Node[newCapacity];

        for (Node node : table) {
            while (node != null) {
                Node next = node.next;
                int newIndex = (node.key == null) ? 0 : Math.abs(node.key.hashCode()) % newCapacity;

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }

        table = newTable;
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
