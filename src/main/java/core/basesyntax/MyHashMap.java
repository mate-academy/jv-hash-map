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
        Node<K,V> tabNode;
        int n;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        int hashValue = hash(key);
        int idx = indexFor(hashValue,n);
        if ((tabNode = tab[idx]) == null) {
            tab[idx] = newNode(hashValue, key, value, null);
        } else {
            Node<K,V> temp;
            K k;
            if (tabNode.hash == hashValue && ((k = tabNode.key) == key
                    || (key != null && key.equals(k)))) {
                temp = tabNode;
            } else {
                for (int in = 0; ; ++in) {
                    if ((temp = tabNode.next) == null) {
                        tabNode.next = newNode(hashValue, key, value, null);
                        break;
                    }
                    if (temp.hash == hashValue && ((k = temp.key) == key
                            || (key != null && key.equals(k)))) {
                        break;
                    }
                    tabNode = temp;
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
            newThr = (int) (INIT_LOAD_FACTOR * INIT_SIZE);
        }
        threshold = newThr;
        Node<K,V>[] newTab = (Node<K,V>[]) new Node[newCap];
        if (oldTab != null) {
            transfer(newTab);
        }
        table = newTab;
        return newTab;
    }

    private void transfer(Node<K,V>[] newTab) {
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap = newTab.length;
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> temp = oldTab[j];
            if (temp != null) {
                oldTab[j] = null;
                do {
                    Node<K,V> next = temp.next;
                    int i = indexFor(temp.hash, newCap);
                    temp.next = newTab[i];
                    newTab[i] = temp;
                    temp = next;
                } while (temp != null);
            }
        }
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    private Node<K,V> getNode(int hash, Object key) {
        Node<K, V>[] tab = table;
        Node<K, V> tabNode;
        Node<K, V> temp;
        int n;
        K k;
        if ((tab != null) && ((n = tab.length) > 0)
                && (tabNode = tab[(n - 1) & hash]) != null) {
            if (tabNode.hash == hash && ((k = tabNode.key) == key
                    || (key != null && key.equals(k)))) {
                return tabNode;
            }
            if ((temp = tabNode.next) != null) {
                do {
                    if (temp.hash == hash && ((k = temp.key) == key
                            || (key != null && key.equals(k)))) {
                        return temp;
                    }
                } while ((temp = temp.next) != null);
            }
        }
        return null;
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static int indexFor(int hashCode, int lengthArray) {
        // hashCode % lengthArray
        return (hashCode & (lengthArray - 1));
    }
}
