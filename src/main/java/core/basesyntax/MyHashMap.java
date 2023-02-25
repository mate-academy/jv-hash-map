package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> oldNode = table[getIndexFromHash(key)];
        if (oldNode == null) {
            table[getIndexFromHash(key)] = newNode;
            size++;
            resize();
        } else {
            while (oldNode != null) {
                if (oldNode.key == newNode.key || oldNode.key != null
                        && oldNode.value.equals(newNode.value)) {
                    oldNode.value = newNode.value;
                    break;
                } else if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    break;
                }
                oldNode = oldNode.next;
            }
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndexFromHash(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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

    private int getIndexFromHash(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode() % table.length);
    }

    private void resize() {
        if (size > table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            size = 0;
            table = new Node[oldTable.length * RESIZE_FACTOR];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
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
