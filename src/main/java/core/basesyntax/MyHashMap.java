package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K,V> currentNode = table[index];
        Node<K, V> myNode = new Node<>(key, value);
        while (currentNode != null) {
            if (currentNode.key == key
                    || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = myNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[index] = myNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = getIndex(key);
        Node<K,V> node = table[index];
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
        size = 0;
        int newLength = table.length * GROW_FACTOR;
        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        for (Node<K, V> current : oldTable) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
