package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int SHIFT_BY_ONE = 1;
    private Node<K, V>[] table;
    private int size;
    private int maxCapacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        maxCapacity = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (maxCapacity < size) {
            resize();
        }
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> node = table[index];
            while (node != null) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    node.value = newNode.value;
                    return;
                } else if (node.next == null) {
                    node.next = newNode;
                    size++;
                    return;
                }
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << SHIFT_BY_ONE];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        maxCapacity = (int) (table.length * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
