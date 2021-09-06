package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
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
                if (checkKeys(table[i].key,key)) {
                    return table[i].value;
                }
                if (table[i].next != null) {
                    suppNode = table[i].next;
                    while (suppNode != null) {
                        if (checkKeys(suppNode.key,key)) {
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

    private void checkCapacity() {
        if (size == threshold) {
            resize();
        }
    }

    private Node<K, V>[] resize() {
        size = 0;
        threshold *= RESIZE_COEFFICIENT;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_COEFFICIENT];
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
            table[hash(key)] = new Node<>(key, value, hash,null);
            size++;
        } else {
            Node<K, V> suppNode;
            for (int i = 0; ; ++i) {
                if (checkKeys(node.key,key)) {
                    node.value = value;
                    break;
                }
                if ((suppNode = node.next) == null) {
                    node.next = new Node<>(key, value, hash, null);
                    size++;
                    break;
                }
                node = suppNode;
            }
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() % table.length
                : key.hashCode() % table.length;
    }

    private boolean checkKeys(K firstKey, K secondKey) {
        return (firstKey == secondKey || (firstKey != null && firstKey.equals(secondKey)));
    }
}
