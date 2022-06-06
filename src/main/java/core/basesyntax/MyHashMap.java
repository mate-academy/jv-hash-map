package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEF_CAPACITY = 1 << 4;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        capacity = DEF_CAPACITY;
        threshold = (int) (DEF_CAPACITY * LOAD_FACTOR);
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int hash = hashCode(key);
        Node<K, V> node = findNode(key);
        if (node != null) {
            node.value = value;
        } else {
            if (threshold == size) {
                resize();
            }
            node = table[hash];
            if (node == null) {
                table[hash] = new Node<>(hash, key, value, null);
            } else {
                while (node.next != null) {
                    node = node.next;
                }
                node.next = new Node<>(hash, key, value, null);
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        capacity = capacity << 1;
        threshold = threshold << 1;

        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> node = table[hashCode(key)];
        while (node != null) {
            if (Node.keysEquals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    int hashCode(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        static boolean keysEquals(Object key1, Object key2) {
            return key1 == key2 || key1 != null && key1.equals(key2);
        }
    }
}
