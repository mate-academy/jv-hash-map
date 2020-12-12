package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float loadFactor = 0.75f;
    private static final short GROWTH_FACTOR = 2;
    private int threshold;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
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

    private void resize(){
        capacity = capacity * GROWTH_FACTOR;
        threshold = (int) (capacity * loadFactor);
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
    };

    private void put(Node<K, V> node, Node<K, V>[] table) {
        int hash = getHash(node);
        if (table[hash] != null) {
            Node<K, V> tableNode = table[hash];
            while (tableNode.next != null) {
                if (tableNode.key.equals(node.key)) {
                    tableNode.value = node.value;
                    return;
                }
                tableNode = tableNode.next;
            }
            tableNode.next = node;
        } else {
            table[hash] = node;
        }
    }

    /*
    returns hash value (position in the table) for rearrangement of the
    table after resize;
     */
    private int getHash(Node<K, V> node){
        return node.key == null ? 0 : node.key.hashCode() % capacity;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private int hash;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
