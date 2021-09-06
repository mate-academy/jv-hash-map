package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int currentCapacity;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        addNode(key, value, hash(key));
    }

    @Override
    public V getValue(K key) {
        Node<K, V> suppNode;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                if (table[i].key == key
                        || (table[i].key != null && table[i].key.equals(key))) {
                    return table[i].value;
                }
                if (table[i].next != null) {
                    suppNode = table[i].next;
                    while (suppNode != null) {
                        if (suppNode.key == key
                                || (suppNode.key != null && suppNode.key.equals(key))) {
                            return suppNode.value;
                        }
                        suppNode = suppNode.next;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapacity() {
        if ((size + 1) > threshold) {
            resize();
        }
    }

    private Node<K, V>[] resize() {
        size = 0;
        currentCapacity = table.length * 2;
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[currentCapacity];
        Node<K, V> suppNode;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                addNode(oldTable[i].key, oldTable[i].value, hash(oldTable[i].key));
                if (oldTable[i].next != null) {
                    suppNode = oldTable[i].next;
                    while (suppNode != null) {
                        addNode(suppNode.key, suppNode.value, hash(suppNode.key));
                        suppNode = suppNode.next;
                    }
                }
            }
        }
        return table;
    }

    private void addNode(K key, V value, int hash) {
        Node<K, V> node;
        if ((node = table[hash(key)]) == null) {
            table[hash(key)] = newNode(key, value, hash,null);
            size++;
        } else {
            Node<K, V> suppNode;
            for (int i = 0; ; ++i) {
                if (node.key == key || (node.key != null && node.key.equals(key))) {
                    node.value = value;
                    break;
                }
                if ((suppNode = node.next) == null) {
                    node.next = newNode(key, value, hash,null);
                    size++;
                    break;
                }
                node = suppNode;
            }
        }
    }

    private Node<K, V> newNode(K key, V value, int hash, Node<K, V> next) {
        return new Node<>(key, value, hash, next);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() % currentCapacity
                : key.hashCode() % currentCapacity;
    }
}
