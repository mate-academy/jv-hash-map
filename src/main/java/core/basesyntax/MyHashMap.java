package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTORY = 0.75;
    private Node[] hashMap;
    private int size;

    public MyHashMap() {
        this.hashMap = new Node[DEFAULT_CAPACITY];
    }

    public class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private MyHashMap.Node next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        addNodeToArray(hashMap, key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node currentNode = hashMap[getHashCode(key) % hashMap.length];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                return (V) currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node[] addNodeToArray(Node[] array, K key, V value) {
        int keyHashCode = getHashCode(key);
        Node node = new Node(keyHashCode, key, value);
        int index = keyHashCode % array.length;
        Node currentNode = array[index];
        if (currentNode != null) {
            Node oldNode = currentNode;
            while (currentNode != null) {
                oldNode = currentNode;
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    size--;
                    return array;
                }
                currentNode = currentNode.next;
            }
            oldNode.next = node;
        } else {
            array[index] = node;
        }
        return array;
    }

    private void resizeIfNeed() {
        if (size == (int) (DEFAULT_LOAD_FACTORY * hashMap.length)) {
            Node[] newHashMap = new Node[hashMap.length * 2];
            for (Node<K, V> node: hashMap) {
                while (node != null) {
                    addNodeToArray(newHashMap, node.key, node.value);
                    node = node.next;
                }
            }
            hashMap = newHashMap;
        }
    }

    private int getHashCode(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }
}
