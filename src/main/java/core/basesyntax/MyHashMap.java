package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int trashould;
    private int size;
    private Node<K, V>[] table;

    {
        table = new Node[DEFAULT_CAPACITY];
        trashould = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == trashould) {
            resize();
        }
        Node<K, V> newNode = new Node(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K,V> previousNode = table[index];
            while (previousNode != null) {
                if (previousNode.key == key
                        || (previousNode.key != null && previousNode.key.equals(key))) {
                    previousNode.value = value;
                    return;
                }
                if (previousNode.next == null) {
                    previousNode.next = newNode;
                    size++;
                    return;
                } else {
                    previousNode = previousNode.next;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] previousTable = table;
        table = new Node[previousTable.length * 2];
        trashould = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node: previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }
}
