package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int hash = getIndex(key);
        
        if (size > threshold) {
            resize();
        }

        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> searchNode = table[hash];

            while (searchNode != null) {
                if (isKeysEquals(searchNode.key, key)) {
                    searchNode.value = value;
                    break;
                } else if (searchNode.next == null) {
                    searchNode.next = new Node<>(key, value, null);
                    size++;
                    break;
                } else {
                    searchNode = searchNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getIndex(key);
        Node<K, V> searchNode = table[hash];

        while (searchNode != null) {
            if (isKeysEquals(searchNode.key, key)) {
                return searchNode.value;
            }
            searchNode = searchNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return Math.abs((key == null) ? 0 : (key.hashCode() % table.length));
    }

    private boolean isKeysEquals(K firstKey, K secondKey) {
        return firstKey == secondKey || (secondKey != null && secondKey.equals(firstKey));
    }

    private void resize() {
        int newInitialCapacity = table.length * 2;
        threshold = (int) (DEFAULT_LOAD_FACTOR * newInitialCapacity);
        Node<K, V>[] oldTable = table;
        table = new Node[newInitialCapacity];
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K,V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];

            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
