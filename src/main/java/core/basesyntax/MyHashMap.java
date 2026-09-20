package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;

    private int size;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        int index = this.getIndex(key, this.table);
        Node<K, V> currentNode = this.table[index];

        while (currentNode != null) {
            boolean sameKey = key == currentNode.key
                    || (key != null && key.equals(currentNode.key));
            if (sameKey) {
                currentNode.value = value;
                return;
            }

            currentNode = currentNode.next;
        }

        if (this.size + 1 > table.length * LOAD_FACTOR) {
            this.resize();
            index = this.getIndex(key, this.table);
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        this.size++;
    }

    @Override
    public V getValue(K key) {
        int index = this.getIndex(key, this.table);
        Node<K, V> currentNode = this.table[index];

        while (currentNode != null) {
            boolean sameKey = key == currentNode.key
                    || (key != null && key.equals(currentNode.key));
            if (sameKey) {
                return currentNode.value;
            }

            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private int getIndex(K key, Node<K, V>[] targetTable) {
        if (key == null) {
            return 0;
        }

        int remainder = key.hashCode() % targetTable.length;
        return Math.abs(remainder);
    }

    private void resize() {
        Node<K, V>[] oldTable = this.table;
        Node<K, V>[] newTable = new Node[this.table.length * RESIZE_FACTOR];

        for (Node<K, V> currNode : oldTable) {
            while (currNode != null) {
                Node<K, V> nextNode = currNode.next;
                int newIndex = this.getIndex(currNode.key, newTable);
                currNode.next = newTable[newIndex];
                newTable[newIndex] = currNode;
                currNode = nextNode;
            }
        }

        this.table = newTable;
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
