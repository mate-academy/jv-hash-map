package core.basesyntax;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }

        @Override
        public final int hashCode() {
            int result = 17;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (value == null ? 0 : value.hashCode());
            return result;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) o;
            return (key == node.key || key != null && key.equals(node.key))
                    && (value == node.value || value != null && value.equals(node.value));
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = transfer();
        }
        Node<K, V> first;
        if ((first = table[getIndex(key)]) == null) {
            table[getIndex(key)] = new Node<>(hash(key), key, value, null);
        } else {
            if (first.hash == hash(key)
                    && (first.key == key || (key != null && key.equals(first.key)))) {
                first.value = value;
                return;
            }
            Node<K, V> e;
            Node<K, V> tail;
            if ((e = first.next) != null) {
                do {
                    tail = e;
                    if (e.hash == hash(key)
                            && (e.key == key || (key != null && key.equals(e.key)))) {
                        e.value = value;
                        return;
                    }
                } while ((e = e.next) != null);
                tail.next = new Node<>(hash(key), key, value, null);
            } else {
                first.next = new Node<>(hash(key), key, value, null);
            }
        }
        size++;
        if (size > threshold) {
            transfer();
        }
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

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private Node<K,V> getNode(K key) {
        Node<K,V> first;
        if (table != null && (table.length) > 0 && ((first = table[getIndex(key)]) != null)) {
            if (first.hash == hash(key)
                    && (first.key == key || (key != null && key.equals(first.key)))) {
                return first;
            }
            Node<K, V> e;
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash(key)
                            && (e.key == key || (key != null && key.equals(e.key)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    private int getIndex(K key) {
        return hash(key) & (table.length - 1);
    }

    private int resize() {
        int oldCap = (table == null) ? 0 : table.length;
        int oldThr = threshold;
        int newCap;
        int newThr;
        if (oldCap == 0) {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        } else {
            newCap = oldCap << 1;
            newThr = oldThr << 1;
        }
        threshold = newThr;
        return newCap;
    }

    private Node<K, V>[] transfer() {
        Node<K, V>[] oldTab = table;
        table = (Node<K, V>[]) new Node[resize()];
        size = 0;
        if (oldTab != null) {
            for (Node<K, V> node : oldTab) {
                if (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                    while (node != null) {
                        put(node.key, node.value);
                        node = node.next;
                    }
                }
            }
        }
        return table;
    }
}
