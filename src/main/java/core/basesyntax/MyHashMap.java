package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    Node<K, V>[] table;
    private int size;
    //private float threshold = table.length * DEFAULT_LOAD_FACTOR;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        //if (size + 1 > threshold) {
        //    resize();
        //}
        int positionInTable = hash(key) % table.length;
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        table[positionInTable] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        //int hash = key.hashCode();
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {

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
