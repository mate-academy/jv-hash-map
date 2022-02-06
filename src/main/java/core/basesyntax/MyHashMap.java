package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private final int defaultInitialCapacity = 16;
    private final float defaultLoadFactor = 0.75f;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = 0;
        size = 0;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private Node<K, V>[] resize() {
        final Node<K, V>[] oldTab = table;
        int oldCap = ((table == null) ? 0 : table.length);
        int oldThr = threshold;
        int newThr;
        int newCap;
        if (oldCap > 0) {
            newCap = oldCap << 1;
            newThr = oldThr << 1;
        } else {
            newThr = (int) (defaultInitialCapacity * defaultLoadFactor);
            newCap = defaultInitialCapacity;
        }
        threshold = newThr;
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int i = 0; i < oldTab.length; i++) {
                Node<K, V> node;
                if ((node = oldTab[i]) != null) {
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
        }
        return newTab;
    }

    private void putNode(Node<K, V> current, Node<K, V>[] newTab) {
        current.next = null;
        int index;
        if ((newTab[index = (current.hash & (newTab.length - 1))]) == null) {
            newTab[index] = current;
        } else {
            Node<K, V> tn = newTab[index];
            K key = current.key;
            if (tn.hash == current.hash && (key == tn.key || (key != null && key.equals(tn.key)))) {
                newTab[index].value = current.value;
            } else {
                while (tn.next != null) {
                    tn = tn.next;
                }
                tn.next = current;
            }
        }
    }

    public void putVal(int hash, K key, V value) {
        int n;
        int index;
        if (table == null || (n = table.length) == 0) {
            n = (table = resize()).length;
        }
        if ((table[index = hash & (n - 1)]) == null) {
            table[index] = new Node<>(hash, key, value, null);
        } else {
            for (Node<K, V> tn = table[index]; ; tn = tn.next) {
                if (tn.hash == hash && (key == tn.key || (key != null && key.equals(tn.key)))) {
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

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);

    }

    @Override
    public V getValue(K key) {
        Node<K, V> node;
        if (table != null && (node = table[hash(key) & (table.length - 1)]) != null) {
            if (node.hash == hash(key)
                    && (node.key == key || (key != null && key.equals(node.key)))) {
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

    static class Node<K, V> {
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
