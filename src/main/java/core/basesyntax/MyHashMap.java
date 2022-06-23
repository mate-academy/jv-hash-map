package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_INITIAL_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_INITIAL_FACTOR) {
            updateSize();
        }
        int index = hash(key);
        Node<K, V> newNode = new Node<K, V>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> nextNode = table[index];
            Node<K,V> prevNode = null;
            while (nextNode != null) {
                if (key == nextNode.key || key != null && key.equals(nextNode.key)) {
                    nextNode.value = value;
                    return;
                }
                prevNode = nextNode;
                nextNode = nextNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> newNode = table[index];
        while (newNode != null) {
            if (key == newNode.key || key != null && key.equals(newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void updateSize() {
        Node<K, V>[] nodes = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K,V> node : nodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
