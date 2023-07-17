package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MULTIPLIER = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int tableIndex = findIndex(key);
        Node<K, V> tempNode = new Node<>(key, value);
        if (table[tableIndex] == null) {
            table[tableIndex] = tempNode;
        } else if (isKeyPresent(tempNode, tableIndex)) {
            return;
        } else {
            Node<K, V> node = table[tableIndex];
            while (node.next != null) {
                node = node.next;
            }
            node.next = tempNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
        Node<K, V> node = table[index];
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

    private void resize() {
        final Node<K, V>[] oldTable = table;
        int newCapacity = table.length * MULTIPLIER;
        threshold = (int)(newCapacity * LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            for (int i = 0; node != null; node = node.next) {
                put(node.key, node.value);
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int findIndex(K key) {
        if (key == null) {
            return 0;
        }
        return getHash(key) % table.length;
    }

    private boolean isKeyPresent(Node<K, V> node, int index) {
        Node<K, V> tempNode = table[index];
        while (tempNode != null) {
            if (tempNode.key == node.key
                    || tempNode.key != null && tempNode.key.equals(node.key)) {
                tempNode.value = node.value;
                return true;
            }
            tempNode = tempNode.next;
        }
        return false;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
