package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int THRESHOLD = 8;
    private static int size;
    private int threshold;
    private final float loadFactor;
    private final int initialCapacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.threshold = THRESHOLD;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.initialCapacity = DEFAULT_INITIAL_CAPACITY;
        size = 0;
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        this.threshold = (int) (loadFactor * initialCapacity);
        this.loadFactor = loadFactor;
        this.initialCapacity = initialCapacity;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> temp;
        return (temp = getNode(key)) == null ? null : temp.value;
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

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                return Objects.equals(key, e.getKey())
                        && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }

    private void putVal(int hash, K key, V value) {
        Node<K, V>[] tab = table;
        int n;
        int i;
        if (tab == null || tab.length == 0) {
            tab = resize();
        }
        n = tab.length;
        i = getIndex(n, hash);
        Node<K, V> current = tab[i];
        if (current == null) {
            tab[i] = newNode(hash, key, value, null);
        } else {
            Node<K, V> temp;
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                temp = current;
            } else {
                for (int count = 0; ; ++count) {
                    temp = current.next;
                    if (temp == null) {
                        current.next = newNode(hash, key, value, null);
                        break;
                    }
                    if (temp.hash == hash && (Objects.equals(key, temp.key))) {
                        break;
                    }
                    current = temp;
                }
            }
            if (temp != null) {
                temp.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    private Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThreshold = threshold;
        int newCap = 0;
        int newThreshold = 0;
        if (oldCap > 0) {
            if (oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThreshold = oldThreshold * 2;
                newCap = oldCap * 2;
            }
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        threshold = newThreshold;
        @SuppressWarnings({"unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;

        if (oldTab != null) {
            transfer(oldTab, oldCap, newCap, newTab);
        }
        return newTab;
    }

    private void transfer(Node<K, V>[] oldTab, int oldCap, int newCap, Node<K, V>[] newTab) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K, V> temp = oldTab[j];
            while (temp != null) {
                oldTab[j] = null;
                Node<K, V> tempNext = temp.next;
                int i = getIndex(newCap, temp.hash);
                temp.next = newTab[i];
                newTab[i] = temp;
                temp = tempNext;
            }
        }
    }

    private int getIndex(int newCap, int hash) {
        return Math.abs(hash % newCap);
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private Node<K, V> getNode(Object key) {
        Node<K, V>[] tab = table;
        if (tab == null) {
            return null;
        }
        int n = tab.length;
        int hash = hash(key);
        int i = getIndex(n, hash);
        Node<K, V> temp = tab[i];
        if (temp != null) {
            while (temp != null) {
                if (temp.hash == hash && (Objects.equals(key, temp.key))) {
                    return temp;
                }
                temp = temp.next;
            }
        }
        return null;
    }
}
