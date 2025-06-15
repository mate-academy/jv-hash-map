package core.basesyntax;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final float LOAD_FACTOR = 0.75f;
    public static final int initialCapacity = 16;
    private Node<K, V>[] table;
    private int capacity = 0;
    private int size = 0;
    private float threshold = LOAD_FACTOR * capacity;

    private static class Node<K,V> implements Map.Entry<K, V> {

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

        @Override
        public K getKey() {
            return null;
        }

        @Override
        public V getValue() {
            return null;
        }

        @Override
        public V setValue(V value) {
            return null;
        }

        @Override
        public String toString() {
            return "Node{"
                    + "key=" + key
                    + ", value=" + value
                    + ", next=" + next
                    + '}';
        }
    }

    @Override
    public void put(K key, V value) {

        if (size == 0 || size == threshold) {
            resize();
        }
        if (size == 0 && key == null && table[0] != null) {
            table[0] = new Node<K,V>(0, null, value, null);
            size++;
        }
        if (size > 0) {
            int hash = hash(key);
            int index = (hash & 0x7fffffff) % capacity;
            Node<K, V> current = table[index];

            if (key == null && table[0] == null) { // null kluczem, pierwszy insert dla 0
                table[0] = new Node<>(0, null, value, null);
                size++;
            }
            if (key == null && current.next != null) {

                while (current.next.key == null) {

                    current.next.value = value;
                    break;

                }
            }
            if (key == null) {
                while (current.next == null) {

                    if (current.key == null) {
                        current.value = value;
                        break;
                    }

                    current.next = new Node<>(0, null, value, null);
                    size++;
                    break;

                }
            }

            if (current == null) {

                table[index] = new Node<>(hash(key), key, value, null);
                size++;

            } else {
                while (true) {
                    if (key == null) {
                        break;
                    }
                    final Node<K, V> testNode = current.next;

                    if ((current.next == null) && !Objects.equals(current.key, key)) {

                        current.next = new Node<>(hash(key), key, value, null);
                        size++;
                        break;
                    }
                    if (!current.value.equals(value) && (Objects.equals(current.key, key))) {

                        current.value = value;
                        break;
                    }
                    if (Objects.equals(current.key, key)) {
                        break;
                    }
                    current = testNode;

                }
            }
        }
        if (key == null && table[0] == null) {

            table[0] = new Node<>(0, null, value, null);
            size++;

        }
        if (size == 0 && key != null) {

            int hash = hash(key);
            int index = (hash & 0x7fffffff) % capacity;
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
        }
        //problem - wartośći nodów widoczne w oknie zmienne, brak iterowania po buckecie
        //implementacja Map
        //kolejność

    }

    @Override
    public V getValue(K key) {

        if (table == null) {
            return null;
        }

        Node<K,V> node;
        node = getNode(key);

        return node == null ? null : node.value;

    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "myhashmap{"
                + "table=" + Arrays.toString(table)
                + ", capacity=" + capacity
                + ", size=" + size
                + ", threshold=" + threshold
                + '}';
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    final Node<K,V> getNode(K key) {
        Node<K,V>[] tab;
        Node<K,V> first;
        Node<K,V> e;
        int n;
        int hash;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0
                && (first = tab[(n - 1) & (hash = hash(key))]) != null) {
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

    private void resize() {

        if (table == null) {

            capacity = initialCapacity;
            threshold = initialCapacity * LOAD_FACTOR;
            table = (Node<K, V>[]) new Node[capacity];
            return;
        }

        if (table.length > 0 && size == threshold) {

            int oldCapacity = capacity;

            int newCapacity = capacity * 2;

            threshold = threshold * 2;

            capacity = newCapacity;

            Node<K,V>[] oldTab = table;
            int oldCap = oldTab.length;
            int newCap = capacity;

            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
            table = newTab;
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
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
    }
}

