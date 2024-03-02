package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 5;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1 << 30;

    private transient Node<K,V>[] table;

    private transient int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        Node<K,V>[] tab;
        Node<K,V> p;
        int n;
        int i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n - 1) & hash(key)]) == null) {
            tab[i] = newNode(hash(key), key, value, null);
        } else {
            Node<K,V> e;
            K k;
            if (p.hash == hash(key)
                    && ((k = p.key) == key || (key != null && key.equals(k)))) {
                p.value = value;
                return;
            }
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash(key), key, value, null);
                    if (++size > threshold) {
                        resize();
                    }
                    return;
                }
                if (e.hash == hash(key)
                        && ((k = e.key) == key || (key != null && key.equals(k)))) {
                    e.value = value;
                    return;
                }
                p = e;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap = 0;
        int newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            newCap = oldCap * 2;
            if (oldThr > 0) {
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        if (newThr == 0) {
            float ft = (float)newCap * DEFAULT_LOAD_FACTOR;
            newThr = (newCap < MAXIMUM_CAPACITY && ft
                    < (float)MAXIMUM_CAPACITY ? (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        transfer(oldTab, newTab, oldCap, newCap);
        return newTab;
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    private void transfer(Node<K,V>[] oldTab, Node<K,V>[] newTab, int oldCap, int newCap) {
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else {
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
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> node = table[0];
            while (node != null) {
                if (node.key == null) {
                    return node.value;
                }
                node = node.next;
            }
            return null;
        }
        int hash = hash(key);
        Node<K, V>[] tab = table;
        int n = (tab == null) ? 0 : tab.length;
        if (n == 0) {
            return null;
        }
        Node<K, V> node = tab[(n - 1) & hash];
        while (node != null) {
            if (node.hash == hash && (key == node.key || key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static class Node<K,V> implements Map.Entry<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
