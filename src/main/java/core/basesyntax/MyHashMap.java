package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_CAPACITY = 0.75f;
    private int threshold;

    private Node<K, V>[] nodes;
    private int arraySize;

    public MyHashMap() {
        this.nodes = new Node[DEFAULT_CAPACITY];
        arraySize = 0;
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (threshold == arraySize) {
            resize();
        }

        int hash = findHash(key);

        if (nodes[hash] != null) {
            Node<K, V> findLastNode = nodes[hash];
            boolean findSameNode = true;

            while (findLastNode != null) {
                if (equalsKey(findLastNode, key)) {
                    findLastNode.value = value;
                    findSameNode = false;
                    break;
                } else {
                    if (findLastNode.nextNode == null) {
                        break;
                    } else {
                        findLastNode = findLastNode.nextNode;
                    }
                }
            }

            if (findSameNode) {
                findLastNode.nextNode = new Node<>(hash, key, value, null);
                arraySize++;
            }

        } else {
            nodes[hash] = new Node<>(hash, key, value, null);
            arraySize++;
        }

    }

    @Override
    public V getValue(K key) {
        int hash = findHash(key);

        Node<K, V> findNode = nodes[hash];

        while (findNode != null) {
            if (equalsKey(findNode, key)) {
                return findNode.value;
            }
            findNode = findNode.nextNode;
        }

        return null;
    }

    @Override
    public int getSize() {
        return arraySize;
    }

    private int findHash(K key) {
        if (key == null) {
            return 0;
        }

        return Math.abs(key.hashCode()) % nodes.length;
    }

    private boolean equalsKey(Node<K, V> findtNode, K key) {
        return findtNode.key == key || findtNode.key != null && findtNode.key.equals(key);
    }

    private void resize() {
        int newArrayLength = nodes.length * 2;
        threshold = (int) (newArrayLength * DEFAULT_LOAD_CAPACITY);

        Node<K, V>[] tempNodes = nodes;
        nodes = new Node[newArrayLength];
        arraySize = 0;
        for (int i = 0; i < tempNodes.length; i++) {
            if (tempNodes[i] != null) {
                put(tempNodes[i].key, tempNodes[i].value);
                if (tempNodes[i].nextNode != null) {
                    Node<K, V> findLastNode = tempNodes[i].nextNode;
                    while (findLastNode != null) {
                        put(findLastNode.key, findLastNode.value);
                        findLastNode = findLastNode.nextNode;
                    }
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(int hash, K key, V value, Node<K, V> nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(nextNode, node.nextNode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, nextNode);
        }
    }
}
