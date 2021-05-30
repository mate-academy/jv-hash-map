package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        this.table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        putVal(hash, key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K,V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putVal(int hash, K key, V value) {
        Node<K, V> p = table[hash];
        if (p == null) {
            p = new Node<>(hash, key, value, null);
            table[hash] = p;
        } else {
            if (((key == p.key) || (key != null && key.equals(p.key)))) {
                p.value = value;
                return;
            }
            while (p.next != null) {
                p = p.next;
                if (((key == p.key) || (key != null && key.equals(p.key)))) {
                    p.value = value;
                    return;
                }
            }
            p.next = new Node<>(hash, key, value, null);
        }
        if (size++ > threshold) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        capacity = oldCap << 1; //double capacity
        threshold = threshold << 1; // double threshold

        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[capacity];
        table = newTab;
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                while (e != null) {
                    put(e.key,e.value);
                    e = e.next;
                }
            }
        }
    }

    private Node<K,V> getNode(Object key) {
        Node<K,V> first;
        Node<K,V> e;
        if (table != null
                    && (first = table[hash(key)]) != null) {
            if (((key == first.key)
                    || (key != null && key.equals(first.key)))) {
                return first;
            }
            if ((e = first.next) != null) {
                do {
                    if (key == e.key
                            || (key != null && key.equals(e.key))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode())) % capacity;
    }

    private static class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
