package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int INIT_SIZE = 16;
    private static final float INIT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

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
        Node<K,V>[] tab;
        if ((tab = table) == null || tab.length == 0) {
            return null;
        }
        int hashValue = hash(key);
        int idx = indexFor(hashValue,tab.length);
        K k;
        for (Node<K,V> temp = tab[idx]; temp != null; temp = temp.next) {
            if (temp.hash == hashValue && ((k = temp.key) == key
                    || (key != null && key.equals(k)))) {
                return (V) temp.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K,V> implements Entry<K,V> {
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

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private static int indexFor(int hashCode, int lengthArray) {
        // hashCode % lengthArray
        return (hashCode & (lengthArray - 1));
    }
}
