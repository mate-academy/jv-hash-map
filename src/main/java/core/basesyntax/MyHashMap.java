package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int i = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if ((table[i]) == null) {
            table[i] = newNode;
            size++;
            return;
        }
        putValueInTree(table[i], newNode);
    }

    @Override
    public V getValue(K key) {
        if ((size == 0)) {
            return null;
        }
        int i = getIndex(key);
        Node<K, V> currentNode;
        if ((currentNode = table[i]) != null) {
            do {
                if ((currentNode.key == key) || ((key != null)
                        && key.equals(currentNode.key))) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValueInTree(Node<K, V> rootNode, Node<K, V> growNode) {
        if ((growNode.key == rootNode.key) || ((growNode.key != null)
                && growNode.key.equals(rootNode.key))) {
            rootNode.value = growNode.value;
            return;
        }
        if (rootNode.next == null) {
            rootNode.next = growNode;
            size++;
            return;
        }
        rootNode = rootNode.next;
        putValueInTree(rootNode, growNode);
    }

    private void resize() {
        int newSize;
        if ((newSize = table.length << 1) <= MAXIMUM_CAPACITY) {
            Node<K, V>[] tempTable = table;
            table = (Node<K, V>[]) new Node[newSize];
            size = 0;
            for (Node<K, V> e : tempTable) {
                for (; e != null; e = e.next) {
                    put(e.key, e.value);
                }
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
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
