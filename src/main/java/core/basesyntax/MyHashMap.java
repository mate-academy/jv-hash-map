package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold;
    private Node<K,V>[]table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resizeIfCapacityFull();
        }
        int tableIndex = findIndex(key);
        Node<K,V> tempNode = new Node<>(hashCodeOfTheKey(key), key, value);
        if (table[tableIndex] == null) {
            table[tableIndex] = tempNode;
        } else if (isKeyExistInMap(tempNode, tableIndex)) {
            return;
        } else {
            Node<K,V> node = table[tableIndex];
            while (node.next != null) {
                node = node.next;
            }
            node.next = tempNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int enteredIndex = findIndex(key);
        Node<K,V> node = table[enteredIndex];
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

    private void resizeIfCapacityFull() {
        int newCapacity = INITIAL_CAPACITY * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K,V> targetNode = table[i];
            while (targetNode != null) {
                Node<K,V> nextNode = targetNode.next;
                int newIndex = targetNode.hash & (newCapacity - 1);
                targetNode.next = newTable[newIndex];
                newTable[newIndex] = targetNode;
                targetNode = nextNode;
            }
        }
        table = newTable;
    }

    private int hashCodeOfTheKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int findIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return hashCodeOfTheKey(key) % table.length;
        }
    }

    private boolean isKeyExistInMap(Node<K,V> node, int index) {
        Node<K,V> tempNode = table[index];
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

    private class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
