package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int tableCapacity = table.length;
        int index;
        if ((table[index = hash & (tableCapacity - 1)]) == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            for (Node<K, V> tn = table[index]; ; tn = tn.next) {
                if (key == tn.key || (key != null && key.equals(tn.key))) {
                    tn.value = value;
                    return;
                }
                if (tn.next == null) {
                    tn.next = new Node<>(hash, key, value, null);
                    break;
                }
            }
        }
        if (++size > threshold) {
            table = resize();
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private Node<K, V>[] resize() {
        final Node<K, V>[] oldTab = table;
        int newThr = threshold << 1;
        int newCap = table.length << 1;;
        threshold = newThr;
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        for (int i = 0; i < oldTab.length; i++) {
            Node<K, V> node = oldTab[i];
            if ((node) != null) {
                oldTab[i] = null;
                if (node.next == null) {
                    newTab[node.hash & (newCap - 1)] = node;
                } else {
                    while (node != null) {
                        final Node<K, V> current = node;
                        node = node.next;
                        putNode(current, newTab);
                    }
                }
            }
        }
        return newTab;
    }

    private void putNode(Node<K, V> current, Node<K, V>[] newTab) {
        current.next = null;
        int index = (current.hash & (newTab.length - 1));
        if ((newTab[index]) == null) {
            newTab[index] = current;
        } else {
            Node<K, V> tn = newTab[index];
            K key = current.key;
            if (key == tn.key || (key != null && key.equals(tn.key))) {
                newTab[index].value = current.value;
            } else {
                while (tn.next != null) {
                    tn = tn.next;
                }
                tn.next = current;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) & (table.length - 1);
        Node<K, V> node = table[index];
        if (node != null) {
            if (node.key == key || (key != null && key.equals(node.key))) {
                return node.value;
            } else {
                while (node != null) {
                    if ((node.key == key || (key != null && key.equals(node.key)))) {
                        return node.value;
                    }
                    node = node.next;
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
