package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node(key, value, null);
            size++;
        } else {
            Node<K, V> sameNode = findNodeInBucket(node, key);
            if (sameNode != null) {
                sameNode.value = value;
            } else {
                Node<K, V> tail = getTail(node);
                tail.next = new Node(key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> sameNode = findNodeInBucket(table[index], key);
        if (sameNode != null) {
            return sameNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        threshold = (int) (table.length * 2 * DEFAULT_LOAD_FACTOR);
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K,V> currentNode = oldTable[i];
            if (currentNode == null) {
                continue;
            }
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private Node<K, V> findNodeInBucket(Node<K, V> node, K key) {
        while (node != null) {
            if (node.key == key || (key != null && key.equals(node.key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private Node<K, V> getTail(Node<K, V> head) {
        while (head.next != null) {
            head = head.next;
        }
        return head;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
