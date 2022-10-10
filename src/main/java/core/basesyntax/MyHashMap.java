package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    private int capacity = INITIAL_CAPACITY;
    private int size = 0;
    private int threshold = (int) (capacity * LOAD_FACTOR);

    private int getHash(Object key) {
        return Math.abs(key == null ? 0 : key.hashCode() % capacity);
    }

    private static class Node<K,V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.next = next;
            this.value = value;
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public K getKey() {
            return key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (value == null ? 0 : value.hashCode());
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (next == null ? 0 : next.hashCode());
            return result;
        }
    }

    private void putInResize(Node<K,V>[] newTable, Node<K,V> oldNode, int hash) {
        boolean isReplaced = false;
        Node<K,V> target;
        Node<K,V> temp;

        if (newTable[hash] != null && !newTable[hash].getKey().equals(oldNode.getKey())) {
            target = newTable[hash];
            if (target.next == null) {
                target.next = oldNode;
            } else {
                while (target.next != null) {
                    if (target.next.getKey().equals(oldNode.getKey())) {
                        oldNode.next = target.next.next;
                        target.next = oldNode;
                        isReplaced = true;
                        break;
                    }
                    if (!target.getKey().equals(oldNode.getKey())) {
                        target = target.next;
                    }
                }
                if (!isReplaced) {
                    target.next = oldNode;
                }
            }
        } else {
            temp = oldNode;
            newTable[hash] = temp;
            newTable[hash].next = null;
        }
    }

    private Node<K,V> [] resize() {
        threshold = threshold * 2;
        capacity = capacity * 2;
        Node<K,V> [] newTable = (Node<K, V>[]) new Node[capacity];

        int elementInOld = 0;
        while (elementInOld < table.length) {
            Node<K,V> oldNode = table[elementInOld];
            if (oldNode != null && oldNode.next == null) {
                Node<K,V> newNode = new Node<>(getHash(oldNode.getKey()),
                        oldNode.getKey(), oldNode.getValue(), null);
                //newTable[newNode.hash] = oldNode;
                putInResize(newTable, oldNode, newNode.hash);
            }
            if (oldNode != null && oldNode.next != null) {
                while (oldNode != null) {
                    Node<K,V> next = oldNode.next;
                    Node<K,V> newNode = new Node<>(getHash(oldNode.getKey()),
                            oldNode.getKey(), oldNode.getValue(), null);
                    //newTable[newNode.hash] = oldNode;
                    putInResize(newTable, oldNode, newNode.hash);
                    oldNode = next;
                }
            }
            elementInOld++;
        }
        return newTable;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            table = resize();
        }
        boolean isReplaced = false;
        Node<K,V> element = new Node<>(getHash(key), key, value, null);
        Node<K,V> target;

        if (table[element.hash] != null && (table[element.hash].getKey() == element.getKey()
                || table[element.hash].getKey() != null
                && table[element.hash].getKey().equals(element.getKey()))) {
            element.next = table[element.hash].next;
            table[element.hash] = element;

        } else if (table[element.hash] != null
                && !table[element.hash].getKey().equals(element.getKey())) {
            target = table[element.hash];

            while (target.next != null) {
                if (target.next.key == element.key
                            || target.next.key != null && target.next.key.equals(element.key)) {
                    element.next = target.next.next;
                    target.next = element;
                    isReplaced = true;
                    break;
                }
                target = target.next;
            }
            if (!isReplaced) {
                target.next = element;
                size++;
            }
        } else {
            table[element.hash] = element;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> target = table[getHash(key)];
        if (target == null) {
            return null;
        }
        if (target.getKey() == key || target.getKey() != null && target.getKey().equals(key)) {
            return table[getHash(key)].getValue();
        } else {
            while (target != null) {
                if (target.getKey() == key || target.getKey() != null
                        && target.getKey().equals(key)) {
                    return target.getValue();
                }
                target = target.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }
}
