package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
    }

    private static class Node<K,V> {
        protected final int hash;
        protected final K key;
        protected V value;
        protected Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        Node<K,V>[] tab;
        Node<K,V> p;
        int n;
        int i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = new Node<>(hash, key, value, null);
        } else {
            Node<K,V> e;
            K k;
            if (p.hash == hash
                    && ((k = p.key) == key || (key != null && key.equals(k)))) {
                e = p;
            } else {
                while (true) {
                    if ((e = p.next) == null) {
                        p.next = new Node<>(hash, key, value, null);
                        break;
                    }
                    if (e.hash == hash
                            && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        break;
                    }
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                e.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public final V getValue(K key) {
        Node<K,V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    private Node<K,V> getNode(K key) {
        Node<K,V>[] tab;
        Node<K,V> first;
        Node<K,V> e;
        int n;
        int hash;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0
                && (first = tab[(n - 1) & (hash = hash(key))]) != null) {
            if (first.hash == hash // always check first node
                    && ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash
                            && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap;
        int newThr = 0;
        if (oldCap > 0) {
            newCap = oldCap << 1;
            if (oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // double threshold
            }
        } else if (oldThr > 0) { // initial capacity was placed in threshold
            newCap = oldThr;
        } else { // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        threshold = newThr;
        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else { // preserve order
                        Node<K,V> loHead = null;
                        Node<K,V> loTail = null;
                        Node<K,V> hiHead = null;
                        Node<K,V> hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                if (hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
