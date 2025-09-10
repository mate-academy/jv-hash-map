package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_TABLE_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final float loadFactor;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        this.capacity = DEFAULT_TABLE_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Node[capacity];
        this.size = 0;
    }

    private static int spread(int h) {
        return h ^ (h >>> 16);
    }

    private static <K> int hash(K key) {
        return spread((key == null) ? 0 : key.hashCode());
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash);

        if (key == null) {
            for (Node<K, V> node = table[0]; node != null; node = node.next) {
                if (node.key == null) {
                    node.value = value;
                    return;
                }
            }

            Node<K, V> newNode = new Node<>(0, table[0], value, null);
            table[0] = newNode;
            size++;
            return;
        }

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && equals(node.key, key)) {
                node.value = value;
                return;
            }
        }

        Node<K, V> newNode = new Node<>(hash, table[index], value, key);
        table[index] = newNode;
        size++;

        if (size + 1 > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int h = hash(key);
        int index = indexFor(h);

        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (node.hash == h && equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = this.table;
        int oldCapacity = this.capacity;
        int newCapacity = oldCapacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> element : oldTable) {
            while (element != null) {
                Node<K, V> next = element.next;
                int newIndex = element.hash & (newCapacity - 1);
                element.next = newTable[newIndex];
                newTable[newIndex] = element;
                element = next;
            }
        }
        int newThr = (int) (newCapacity * this.loadFactor);

        this.capacity = newCapacity;
        this.table = newTable;
        this.threshold = newThr;
    }

    private int indexFor(int n) {
        return n & (capacity - 1);
    }

    private boolean equals(K keyOne, K keyTwo) {
        return keyOne == keyTwo || (keyOne != null && keyOne.equals(keyTwo));
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, Node<K, V> next, V value, K key) {
            this.hash = hash;
            this.next = next;
            this.value = value;
            this.key = key;
        }
    }
}
