package core.basesyntax;

import java.util.Map;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int INIT_SIZE = 16;
    private static final float INIT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    private class Node<K,V> implements Map.Entry<K,V> {
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

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final int hashCode() {
            hash = 31;
            hash = hash * 17 + key.hashCode();
            return hash;
        }

        @Override
        public String toString() {
            return "Node{" + "hash=" + hash + ", key=" + key
                    + ", value=" + value + ", next=" + next + '}';
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K,V>[] tab;
        Node<K,V> p;
        int n;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        int hashValue = hash(key);
        int idx = indexFor(hashValue,n);
        if ((p = tab[idx]) == null) {
            tab[idx] = newNode(hashValue, key, value, null);
        } else {
            Node<K,V> e;
            K k;
            if (p.hash == hashValue && ((k = p.key) == key
                    || (key != null && key.equals(k)))) {
                e = p;
            } else {
                for (int in = 0; ; ++in) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hashValue, key, value, null);
                        break;
                    }
                    if (e.hash == hashValue && ((k = e.key) == key
                            || (key != null && key.equals(k)))) {
                        break;
                    }
                    p = e;
                }
            }
            if (e != null) {
                e.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap;
        int newThr;
        if (oldCap > 0) {
            newCap = oldCap << 1;
            newThr = oldThr << 1;
        } else {
            newCap = INIT_SIZE;
            newThr = (int)(INIT_LOAD_FACTOR * INIT_SIZE);
        }
        threshold = newThr;
        Node<K,V>[] newTab = (Node<K,V>[]) new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            transfer(oldTab,newTab);
        }
        return newTab;
    }

    private void transfer(Node<K,V>[] oldTab, Node<K,V>[] newTab) {
        int oldCap = oldTab.length;
        int newCap = newTab.length;
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null) {
                    int idx = indexFor(e.hash, newCap);
                    newTab[idx] = e;
                } else {
                    Node<K, V> loHead = null;
                    Node<K, V> loTail = null;
                    Node<K, V> hiHead = null;
                    Node<K, V> hiTail = null;
                    Node<K, V> next;
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

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    private Node<K,V> getNode(int hash, Object key) {
        Node<K, V>[] tab = table;
        Node<K, V> p;
        Node<K, V> e;
        int n;
        K k;
        if ((tab != null) && ((n = tab.length) > 0)
                && (p = tab[(n - 1) & hash]) != null) {
            if (p.hash == hash && ((k = p.key) == key
                    || (key != null && key.equals(k)))) {
                return p;
            }
            if ((e = p.next) != null) {
                do {
                    if (e.hash == hash && ((k = e.key) == key
                            || (key != null && key.equals(k)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static int indexFor(int hh, int length) {
        // hh % length
        return (hh & (length - 1));
    }
}
