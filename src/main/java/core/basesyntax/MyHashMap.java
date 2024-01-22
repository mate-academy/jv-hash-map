package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int RESIZE_FACTOR = 2;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        putToTable(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        return findNodeByKey(key, currentNode).value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        if (size > (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * RESIZE_FACTOR];
            size = 0;
            for (Node<K, V> headNode : oldTable) {
                while (headNode != null) {
                    putToTable(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }

    private void putToTable(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node(key, value, null);
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                if (isEqualsKey(key, currentNode)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (isEqualsKey(key, currentNode)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = new Node(key, value, null);
        }
        size++;
    }

    private static <K, V> boolean isEqualsKey(K key, Node<K, V> currentNode) {
        return currentNode.key == key || currentNode.key != null && currentNode.key.equals(key);
    }

    private int getIndex(K key) {
        return Math.abs(hash(key) % table.length);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private Node<K, V> findNodeByKey(K key, Node<K, V> currentNode) {
        do {
            if (isEqualsKey(key, currentNode)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
