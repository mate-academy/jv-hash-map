package core.basesyntax;

import static core.basesyntax.MyHashMap.Node.hash;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashTable = new Node[DEFAULT_LENGTH];
    private int size;
    private int arrayLength;

    static class Node<K, V> implements Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        static int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) * (h >> 4);
        }
    }

    public MyHashMap() {
        this.arrayLength = DEFAULT_LENGTH;
    }

    @Override
    public void put(K key, V value) {
        int hashCode = hash(key);
        int index = Math.abs(hashCode) % arrayLength;
        Node<K, V> node = hashTable[index];
        if (node == null) {
            hashTable[index] = new Node(key, value, null);
        } else {
            for (Node<K, V> n = node; true; n = n.next) {
                K nodeKey = n.key;
                if ((key == null && null == nodeKey) || (key != null && key.equals(nodeKey))) {
                    n.value = value;
                    size--;
                    break;
                }
                if (n.next == null) {
                    n.next = new Node<>(key, value, null);
                    break;
                }
            }
        }
        if (++size > arrayLength * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] tempHashMap;
        int newLength = arrayLength * 2;
        Node<K, V>[] newTables = new Node[newLength];
        tempHashMap = hashTable;
        arrayLength = newLength;
        hashTable = newTables;
        size = 0;
        for (Node<K, V> node : tempHashMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

    }

    @Override
    public V getValue(K key) {
        int hashCode = hash(key);
        int index = Math.abs(hashCode) % arrayLength;
        Node<K, V> node = hashTable[index];
        Node<K, V> currentNode;
        for (currentNode = node; currentNode != null; currentNode = currentNode.next) {
            if ((key == null && null == currentNode.key)
                    || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
