package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float LOAD_FACTOR = 0.75f;
    static final int DEFAULT_RESIZE_COUNT = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = DEFAULT_INITIAL_CAPACITY * (int) LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int backet = getKeyBucket(key);
        Node<K, V> node = table[backet];
        Node<K, V> newNode = new Node<>(keyHashCode(key), key, value, null);
        if (node == null) {
            table[backet] = newNode;
        }
        while (node != null) {
            if ((key == node.key) || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                break;
            }
            node = node.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int backet = getKeyBucket(key);
        Node<K, V> node = table[backet];
        while (node != null) {
            if ((key == node.key) || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        threshold *= DEFAULT_RESIZE_COUNT;
        Node<K, V>[] oldTab = table;
        table = new Node[table.length * DEFAULT_RESIZE_COUNT];
        transfer(oldTab);
    }

    private void transfer(Node<K, V>[] oldTab) {
        for (Node<K, V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int keyHashCode(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> DEFAULT_INITIAL_CAPACITY);
    }

    private int getKeyBucket(K key) {
        return keyHashCode(key) & table.length - 1;
    }

    static class Node<K,V> {
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
    }
}
