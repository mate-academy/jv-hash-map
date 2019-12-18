package core.basesyntax;

import static java.lang.Math.abs;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K, V>[] hashTable;
    private int size;
    private static final int INITIAL_CAPACITY = 2;
    private static final double LOAD_FACTOR = 0.75d;
    private int resizeCapacity;

    public MyHashMap() {
        hashTable = new Node[INITIAL_CAPACITY];
        resizeCapacity = (int) (hashTable.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        extendSize();
        Node<K, V> newNode = new Node<K, V>(key, value);
        int index = (key == null) ? 0 : newNode.hash();

        if (hashTable[index] == null) {
            hashTable[index] = new Node<>(null, null);
            hashTable[index].nodes.add(newNode);
            size++;
            return;
        }
        List<Node<K, V>> nodeList = hashTable[index].nodes;
        for (Node node : nodeList) {
            if (Objects.equals(node.key, newNode.key)) {
                node.value = value;
                return;
            }
        }
        nodeList.add(newNode);
        size++;
        return;
    }

    @Override
    public V getValue(K key) {

        int index = (key == null) ? 0 : hash(key);
        if (index < hashTable.length && hashTable[index] != null) {

            List<Node<K, V>> list = hashTable[index].nodes;
            for (Node<K, V> node : list) {
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    private int hash(K key) {
        int hash = abs(31 * key.hashCode());
        return (hashTable.length == 0 || hash == 0) ? 0 : hash % hashTable.length;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {

        private List<Node<K, V>> nodes;
        private int hash;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            nodes = new LinkedList<Node<K, V>>();
        }

        private int hash() {
            return hashCode() % hashTable.length;
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
            return key.equals(node.key);
        }

        @Override
        public int hashCode() {
            hash = key.hashCode() * 31;
            return Math.abs(hash);
        }
    }

    private void arrayResizing() {
        Node<K, V>[] oldHashArray = hashTable;
        hashTable = new Node[oldHashArray.length << 1];
        size = 0;
        for (Node<K, V> node : oldHashArray) {
            if (node != null) {
                for (Node<K, V> newNode : node.nodes) {
                    put(newNode.key, newNode.value);
                }
            }
        }
    }

    private void extendSize() {

        if (size > resizeCapacity) {
            resizeCapacity = resizeCapacity * 2;
            arrayResizing();
        }
    }
}
