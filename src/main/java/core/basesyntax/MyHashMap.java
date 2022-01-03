package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    static final float LOAD_FACTOR = 0.75f;

    private int threshold = (int)(DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);

    private int size;

    private Node<K, V>[] table;

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        fillTable(getIndex(key), key, value);
        if (size > threshold) {
            size = 0;
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null && table[getIndex(key)] != null) {
            Node<K, V> p = table[getIndex(key)];
            do {
                if ((p.key == null && key == null) || (key != null && key.equals(p.key))) {
                    if (p.key == null) {
                        return p.value;
                    }
                    if (key.equals(p.key)) {
                        return p.value;
                    }
                }
            } while ((p = p.next) != null);
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap;
        int newThreshold;
        newCap = oldCap << 1;
        newThreshold = (int) (newCap * LOAD_FACTOR);

        threshold = newThreshold;
        table = (Node<K, V>[]) new Node[newCap];

        transfer(oldTab, oldCap);
    }

    private void transfer(Node<K, V>[] oldTab, int oldCap) {
        for (int i = 0; i < oldCap; i++) {
            if (oldTab[i] != null) {
                Node<K, V> e = oldTab[i];
                if ((e.next) == null) {
                    int index = getIndex(e.key);
                    fillTable(index, e.key, e.value);
                } else {
                    Node<K, V> next;
                    do {
                        next = e.next;
                        fillTable(getIndex(e.key), e.key, e.value);
                    } while ((e = next) != null);
                }
            }
        }
    }

    private void fillTable(int index, K key, V value) {
        Node<K, V> p;
        if ((p = table[index]) == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> e;
            while (true) {
                e = p.next;
                if ((p.key == key || (key != null && key.equals(p.key)))) {
                    p.value = value;
                    break;
                }
                if (e == null) {
                    p.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                p = e;
            }
        }
    }
}
