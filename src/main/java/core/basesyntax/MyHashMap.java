package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (loadFactor * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = hash % table.length;
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> currNode = table[index];
            do {
                if (currNode.hash == hash
                        && (currNode.key == key
                        || (currNode.key != null && currNode.key.equals(key)))) {
                    currNode.value = value;
                    break;
                }
                if (currNode.next == null) {
                    currNode.next = new Node<>(hash, key, value, null);
                    size++;
                    break;
                }
                currNode = currNode.next;
            } while (true);
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash % table.length;
        if (table[index] == null) {
            return null;
        }
        Node<K, V> currNode = table[index];
        do {
            if (currNode.hash == hash
                    && (currNode.key == key
                    || currNode.key != null && currNode.key.equals(key))) {
                return currNode.value;
            }
        } while ((currNode = currNode.next) != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length << 1;
        @SuppressWarnings("unchecked")
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (loadFactor * newCapacity);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int index = node.hash % newCapacity;
                node.next = newTable[index];
                newTable[index] = node;
                node = nextNode;
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        int keyHashCode;
        return key == null ? 0
                : ((keyHashCode = key.hashCode()) ^ (keyHashCode >>> 16)) & 0xfffffff;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
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
