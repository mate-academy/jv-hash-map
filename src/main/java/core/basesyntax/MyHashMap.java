package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTORY = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTORY * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        resize();
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = node;
            size++;
        } else {
            Node<K, V> oldNode = table[getIndex(key)];
            oldNode = getRightNode(oldNode, key);
            if (checkEquals(oldNode.key, key)) {
                oldNode.value = node.value;
                return;
            }
            oldNode.next = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        if (table[getIndex(key)] == null) {
            return null;
        }
        Node<K, V> tempNode = table[getIndex(key)];
        tempNode = getRightNode(tempNode, key);
        return tempNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private boolean needToResize() {
        return size > threshold;
    }

    private void resize() {
        if (needToResize()) {
            Node<K, V>[] oldTable = new Node[table.length];
            System.arraycopy(table, 0, oldTable, 0, table.length);
            table = new Node[oldTable.length * CAPACITY_MULTIPLIER];
            grow();
            Node<K, V> tempNode;
            size = 0;
            for (int i = 0; i < oldTable.length; i++) {
                tempNode = oldTable[i];
                if (tempNode != null) {
                    while (tempNode != null) {
                        put(tempNode.key, tempNode.value);
                        tempNode = tempNode.next;
                    }
                }
            }
        }
    }

    private void grow() {
        threshold = (int) (DEFAULT_LOAD_FACTORY * table.length);
    }

    private boolean checkEquals(Object first, Object second) {
        return first == second || first != null && first.equals(second);
    }

    private Node<K, V> getRightNode(Node<K, V> node, K key) {
        while (node.next != null) {
            if (checkEquals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return node;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
