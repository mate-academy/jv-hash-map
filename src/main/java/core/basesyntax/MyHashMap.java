package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int bucketNumber = getBucketNumber(key);
        if (table[bucketNumber] == null) {
            Node<K, V> newNode = new Node<>(hash(key), key, value, null);
            table[bucketNumber] = newNode;
        } else if (table[bucketNumber].key.equals(key)){
            table[bucketNumber].value = value;
        } else if (key.hashCode() == table[bucketNumber].hashCode()
                && !table[bucketNumber].key.equals(key)) {
            Node<K, V> newNode = new Node<>(hash(key), key, value, null);
            table[bucketNumber].next = newNode;
        } else if (table[bucketNumber] != null) {
            Node<K, V> currentNode = table[bucketNumber];
            while (currentNode != null) {
                if (key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = new Node<>(hash(key), key, value, null);
                }
                currentNode=  currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        return (table[getBucketNumber(key)] == null) ? null
                : table[getBucketNumber(key)].getNodeValue();
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
        return hash(key) % table.length;
    }

    private float getThreshold() {
        return table.length * DEFAULT_LOAD_FACTOR;
    }

    private Node<K, V>[] expandTable() {
        Node<K, V>[] expandTable = new Node[table.length << 1];
        return expandTable;
    }

    ///////////// DELETE TO PRINT ////////////////////
    public void print() {
        int position = 0;
        for (Node<K, V> node : table) {
            System.out.print(position + " ");
            System.out.println(node);
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

        public V getNodeValue() {
            return value;
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
