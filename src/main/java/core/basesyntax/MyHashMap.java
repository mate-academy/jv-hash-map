package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int mapCapacity;
    private Node<K, V>[] table;
    private int threshold;
    private int POSITION_FOR_NULL_KEYS = 0;

    class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }


    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (key == null) {
            if (table[POSITION_FOR_NULL_KEYS] == null) {
                table[POSITION_FOR_NULL_KEYS] = newNode;
                size++;
            } else {
                Node<K, V> node = table[POSITION_FOR_NULL_KEYS];
                if (node.key == null) {
                    node.value = value;
                    return;
                }
                while (node.next != null) {
                    if (node.next.key == null) {
                        node.next.value = value;
                        return;
                    }
                    node = node.next;
                }
                node.next = newNode;
                size++;
            }
        } else {
            Node<K, V> node = table[hash(key)];
            if (node == null) {
                table[hash(key)] = newNode;
                size++;
            } else if (node.key.equals(key)) {
                node.value = value;
            } else {
                while (node.next != null) {
                    if (node.next.key != null && node.next.key.equals(key)) {
                        node.next.value = value;
                    } else {
                        node = node.next;
                    }
                }
                node.next = newNode;
                size++;
            }
        }

        if (size + 1 > threshold) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            }
            if (node.key.equals(key)) {
                return node.value;
            }
            while (node.next != null) {
                if (key == null && node.next.key == null
                        || node.next.key != null && node.next.key.equals(key)) {
                    return node.next.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() % mapCapacity));
    }


    private Node<K, V>[] resize() {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable;
        int newCapacity = mapCapacity << 1;

        if (table == null) {
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            newTable = (Node<K, V>[]) (new Node[DEFAULT_INITIAL_CAPACITY]);
            table = newTable;
            mapCapacity = DEFAULT_INITIAL_CAPACITY;
            return newTable;
        }
        newTable = (Node<K, V>[]) (new Node[newCapacity]);
        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> oldNode : oldTable) {
            if (oldNode != null) {
                put(oldNode.key, oldNode.value);
                while (oldNode.next != null) {
                    put(oldNode.next.key, oldNode.next.value);
                }
            }
        }
        mapCapacity = newCapacity;
        return newTable;
    }
}
