package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private transient Node<K, V> [] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int threshold = (int) (table.length * LOAD_FACTOR);
    private int size;

    @Override
    public void put(K key, V value) {
        int hashCode = hash(key);
        putVal(hashCode, key, value);

    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = index(hash);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            return null;
        }

        while (currentNode != null) {
            if (currentNode.hash == hash && currentNode.key == null && key == null) {
                return currentNode.value;
            }
            if (
                    currentNode.hash == hash
                            && currentNode.key != null
                            && currentNode.key.equals(key)
            ) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;

    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void putVal(int hash, K key, V val) {
        if (getSize() >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, hash, val, null);
        int position = index(hash);
        if (table[position] == null) {
            table[position] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[position];
        while (currentNode != null) {
            if (currentNode.hash == hash && currentNode.key == null && key == null) {
                currentNode.value = val;
                break;
            }

            if (
                    currentNode.hash == hash
                            && currentNode.key != null
                            && currentNode.key.equals(key)
            ) {
                currentNode.value = val;
                break;
            }

            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, hash, val, null);
                size++;
                break;
            }
            currentNode = currentNode.next;
        }

    }

    private int index(int hash) {
        return (table.length - 1) & hash;
    }

    private void resize() {
        Node<K, V> [] newArr = new Node[table.length * 2];
        Node<K, V> [] oldArr = table;
        table = newArr;
        threshold = (int) (newArr.length * LOAD_FACTOR);

        for (int i = 0; i < oldArr.length; i++) {
            if (oldArr[i] == null) {
                continue;
            }
            Node<K, V> currentNode = oldArr[i];
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.next;
                int index = index(currentNode.hash);

                if (newArr[index] == null) {
                    newArr[index] = currentNode;
                    currentNode.next = null;
                } else {
                    Node<K, V> temp = newArr[index];
                    while (temp.next != null) {
                        temp = temp.next;
                    }
                    temp.next = currentNode;
                    currentNode.next = null;
                }

                currentNode = nextNode;
            }

        }

    }

    private static class Node<K, V> implements Map.Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, int hash, V value, Node<K, V> next) {
            this.key = key;
            this.hash = hash;
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
            return null;
        }
    }
}
