package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private MyHashMap.Node<K,V>[] table;
    private int size;
    private int threshold;

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private MyHashMap.Node<K,V> next;

        Node(int hash, K key, V value, MyHashMap.Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value, false);
    }

    @Override
    public V getValue(K key) {
        MyHashMap.Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private final MyHashMap.Node<K,V>[] resize() {
        MyHashMap.Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap;
        int newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY
                    && oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        threshold = newThr;
        MyHashMap.Node<K,V>[] newTab = (MyHashMap.Node<K,V>[])new MyHashMap.Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                MyHashMap.Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else {
                        MyHashMap.Node<K,V> loHead = null;
                        MyHashMap.Node<K,V> loTail = null;
                        MyHashMap.Node<K,V> hiHead = null;
                        MyHashMap.Node<K,V> hiTail = null;
                        MyHashMap.Node<K,V> next;
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

    public final MyHashMap.Node<K,V> getNode(int hash, Object key) {
        MyHashMap.Node<K,V>[] tab;
        MyHashMap.Node<K,V> first;
        MyHashMap.Node<K,V> e;
        int n;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0
                && (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash
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

    public final V putVal(int hash, K key, V value, boolean onlyIfAbsent) {
        MyHashMap.Node<K,V>[] tab;
        MyHashMap.Node<K,V> p;
        int n;
        int i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n - 1) & hash]) == null) {
            tab[i] = new MyHashMap.Node(hash, key, value, null);
        } else {
            MyHashMap.Node<K,V> e;
            K k;
            if (p.hash == hash
                    && ((k = p.key) == key || (key != null && key.equals(k)))) {
                e = p;
            } else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = new MyHashMap.Node(hash, key, value, null);
                        break;
                    }
                    if (e.hash == hash
                            && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        break;
                    }
                    p = e;
                }
            }
            if (e != null) {
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null) {
                    e.value = value;
                }
                return oldValue;
            }
        }
        if (++size > threshold) {
            resize();
        }
        return null;
    }
}
