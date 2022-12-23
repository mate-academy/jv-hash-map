package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = table[getBucketNumber(key)];
        if (currentNode == null) {
            table[getBucketNumber(key)] = new Node<>(hash(key), key, value, null);
            size++;
        } else {
            while (true) {
                if (key == null || key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash(key), key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucketNumber(key)];
        while (currentNode != null) {
            if (key == null || currentNode.key.equals(key)) {
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

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {

    }

    private int getBucketNumber(K key) {
        if (hash(key) < 0) {
            return hash(key) % table.length * -1;
        }
        return hash(key) % table.length;
    }

    private float getThreshold() {
        return table.length * DEFAULT_LOAD_FACTOR;
    }

    private Node<K, V>[] expandTable() {
        Node<K, V>[] expandTable = new Node[table.length << 1];
        return expandTable;
    }

    ///////////// DELETE PRINT ////////////////////
    public void print() {
        int position = 0;
        for (Node<K, V> node : table) {
            System.out.print(position + " ");
            if (node == null || node.next == null) {
            System.out.println(node);
            } else {
                System.out.print(node);
                System.out.println(" --> " + node.next);
            }
            position++;
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        ////////////// DELETE TO STRING /////////////
        @Override
        public String toString() {
            return "Node{" +
                    "hash=" + hash +
                    ", key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
