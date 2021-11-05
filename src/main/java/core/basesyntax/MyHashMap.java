package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else if (ifEqualsKey(table[index], key)) {
            table[index].value = value;
            return;
        } else {
            Node<K, V> currentNode = table[getIndex(key)];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (ifEqualsKey(currentNode, key)) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
            while (currentNode != null) {
                if (ifEqualsKey(currentNode, key)) {
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

    private void resizeIfNeeded() {
        if (size >= threshold) {
            Node<K, V>[] tempTable = table;
            table = new Node[tempTable.length * RESIZE_FACTOR];
            threshold = (int) (table.length * LOAD_FACTOR);
            size = 0;
            for (Node<K, V> currentNode : tempTable) {
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return key != null ? Math.abs(key.hashCode() % table.length) : 0;
    }

    private boolean ifEqualsKey(Node<K, V> node, K key) {
        return (node.key == key
                || node.key != null && node.key.equals(key));
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
