package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_MULTIPLIER = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        resize();
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = node;
        } else {
            Node<K, V> oldNode = table[getIndex(key)];
            oldNode = getRightNode(oldNode, key);
            if (checkEquals(oldNode.key, key)) {
                oldNode.value = node.value;
                return;
            }
            oldNode.next = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
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

    private void resize() {
        if (size > (int) (DEFAULT_LOAD_FACTOR * table.length)) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * CAPACITY_MULTIPLIER];
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

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
