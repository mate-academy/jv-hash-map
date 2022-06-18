package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_BY_TWO = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        resize();
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K, V> node;
        int index = getIndex(key);

        if (table[index] == null) {
            table[index] = newNode(key, value);
        } else {
            node = table[index];
            while (node != null) {
                if (node.hash == hash
                        && (node.key == key || node.key != null
                        && node.key.equals(key))) {
                    node.value = value;
                    return;
                } else {
                    if (node.next == null) {
                        node.next = newNode(key, value);
                        break;
                    }
                }
                node = node.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash(key)
                    && (current.key == key || current.key != null
                    && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int index = hash(key) % table.length;
        return index;
    }

    final void resize() {
        Node<K, V>[] tab;
        int oldCapacity = (table == null) ? 0 : table.length;

        if (oldCapacity > 0) {
            if (threshold < size) {
                tab = table;
                table = new Node[oldCapacity * INCREASE_BY_TWO];
                threshold = (int) (table.length * LOAD_FACTOR);
                transfer(tab);
                tab = table;
            }
        }

        if (table == null || table.length == 0) {
            table = new Node[DEFAULT_CAPACITY];
            threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        }
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(Object key) {
        int h;
        return Math.abs((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16));
    }

    private Node<K, V> newNode(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        return newNode;
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
