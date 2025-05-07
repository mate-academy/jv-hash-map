package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_SIZE = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int size;
    private MyNode<K, V>[] myNodes;

    private static class MyNode<K, V> {
        private final K key;
        private V value;
        private MyNode<K, V> next;

        public MyNode(K key, V value, MyNode<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        myNodes = new MyNode[INITIAL_SIZE];
    }

    @Override
    public void put(K key, V value) {
        if (size >= myNodes.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        MyNode<K, V> currNode = new MyNode<>(key, value, null);
        int index = getIndex(key);
        MyNode<K, V> nodeNew = myNodes[index];
        if (nodeNew == null) {
            myNodes[index] = currNode;
        }
        while (nodeNew != null) {
            if (Objects.equals(nodeNew.key, key)) {
                nodeNew.value = value;
                return;
            }
            if (nodeNew.next == null) {
                nodeNew.next = currNode;
                break;
            }
            nodeNew = nodeNew.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        MyNode<K, V> node = myNodes[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % myNodes.length);
    }

    private void resize() {
        MyNode<K, V>[] nodesArray = myNodes;
        size = 0;
        myNodes = new MyNode[nodesArray.length * RESIZE_COEFFICIENT];
        for (MyNode<K, V> node : nodesArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
