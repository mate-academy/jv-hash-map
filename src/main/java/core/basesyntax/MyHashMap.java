package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_ARRAY_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private double treshHold;
    private Node<K, V> currentNode;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[START_ARRAY_CAPACITY];
        treshHold = table.length * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> nodeToAdd = new Node<>(key == null ? 0 : key.hashCode(), key, value, null);
        int index = getIndex(nodeToAdd.hash);
        if (table[index] == null) {
            table[index] = nodeToAdd;
        } else {
            currentNode = table[index];
            int numberOfElementsInTheBucket = 1;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                numberOfElementsInTheBucket++;
            }
            Node<K, V>[] nodesInTheBucket = new Node[numberOfElementsInTheBucket];
            currentNode = table[index];
            for (int i = 0; i < nodesInTheBucket.length; i++) {
                nodesInTheBucket[i] = currentNode;
                currentNode = currentNode.next;
            }
            for (int i = 0; i < nodesInTheBucket.length; i++) {
                if (checkIfKeyExists(nodesInTheBucket[i], key)) {
                    nodesInTheBucket[i].value = value;
                    if (nodesInTheBucket.length > 1) {
                        nodesInTheBucket[0].next = nodesInTheBucket[1];
                    }
                    table[index] = nodesInTheBucket[0];
                    return;
                }
            }
            if (nodesInTheBucket.length > 1) {
                nodesInTheBucket[numberOfElementsInTheBucket - 1].next = nodeToAdd;
                nodesInTheBucket[0].next = nodesInTheBucket[1];
            } else {
                nodesInTheBucket[0].next = nodeToAdd;
            }
            table[index] = nodesInTheBucket[0];
        }
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        currentNode = table[getIndex(key == null ? 0 : key.hashCode())];
        while (currentNode != null) {
            if (checkIfKeyExists(currentNode, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean checkIfKeyExists(Node<K, V> nodeToCheck, K key) {
        if (nodeToCheck.key == key
                || nodeToCheck.key != null && nodeToCheck.key.equals(key)) {
            return true;
        }
        return false;
    }

    private int getIndex(int hash) {
        return Math.abs(hash) % table.length;
    }

    private void resize() {
        if (size >= treshHold) {
            size = 0;
            Node<K, V>[] newTable = table;
            table = new Node[table.length * 2];
            treshHold = table.length * LOAD_FACTOR;
            for (Node<K, V> current : newTable) {
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
