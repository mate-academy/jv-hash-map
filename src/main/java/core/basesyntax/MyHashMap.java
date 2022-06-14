package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        size++;
        putValue(key, value);
    }

    private void putValue(K key, V value) {
        int hash = hash(key);
        int index;
        Node<K, V> node;
        Node<K, V> oldNode;

        if (table == null || table.length == 0) {
            resize();
        }

        if (table[(index = hash % table.length)] == null) {
            table[index] = newNode(key, value);
        } else {
            node = table[index];
            if (node.hash == hash
                    && (node.key == key || node.key != null && node.key.equals(key))) {
                node.value = value;
                size--;
                return;
            } else {
                while (node != null) {
                    if (node.next == null) {
                        node.next = newNode(key, value);
                        break;
                    } else {
                        if (node.next.hash == hash
                                && (node.next.key == key || node.next.key != null
                                && node.next.key.equals(key))) {
                            oldNode = node.next.next;
                            node.next = newNode(key, value);
                            node.next.next = oldNode;
                            size--;
                            return;
                        }
                    }
                    node = node.next;
                }
            }
        }
        if (size > threshold) {
            resize();
        }

    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int index = hash(key) % table.length;
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash(key)
                        && (current.key == key || current.key != null
                        && current.key.equals(key))) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    final void resize() {
        Node<K, V>[] tab;
        int oldCapacity = (table == null) ? 0 : table.length;

        if (oldCapacity > 0) {
            if (threshold < size) {
                tab = table;
                table = new Node[oldCapacity * 2];
                threshold = threshold * 2;
                transfer(tab);
                tab = table;
            }
        }

        if (table == null || table.length == 0) {
            table = new Node[DEFAULT_CAPACITY];
            threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        }
    }

    private void transfer(Node<K, V>[] tab) {
        Node<K, V> node;
        Node<K, V> nextNode;
        for (int i = 0; i < tab.length; i++) {
            if ((node = tab[i]) != null) {
                tab[i] = null;
                while (node != null) {
                    nextNode = node.next;
                    node.next = null;
                    putValue(node.key, node.value);
                    node = nextNode;
                }
            }
        }
    }

    private final int hash(Object key) {
        int h;
        return Math.abs((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16));
    }

    private Node<K, V> newNode(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        return newNode;
    }

    static class Node<K, V> {
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
