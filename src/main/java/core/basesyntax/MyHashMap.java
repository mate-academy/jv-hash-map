package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
    }

    @Override
    public void put(K key, V value) {
        Node node = new Node();
        node.key = key;
        node.value = value;
        node.next = null;
        int bucketIndex = findingBucketIndex(key);
        if (nodes[bucketIndex] == null) {
            nodes[bucketIndex] = node;
        } else {
            Node tempNode = nodes[bucketIndex];
            Node tempNodePrev = null;
            while (tempNode != null) {
                tempNodePrev = tempNode;
                if (key == null && tempNode.key == null
                        || key != null && key.equals(tempNode.key)) {
                    tempNode.value = value;
                    return;
                }
                tempNode = tempNode.next;
            }
            tempNodePrev.next = node;
        }
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < nodes.length; i++) {
            Node temp = nodes[i];
            while (temp != null) {
                if (key == temp.key || key != null && key.equals(temp.key)) {
                    return (V) temp.value;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findingBucketIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % nodes.length;
        }
    }

    private void resize() {
        if (size == nodes.length * LOAD_FACTOR + 1) {
            size = 0;
            Node<K, V>[] copyNodeArray = nodes;
            nodes = new Node[nodes.length * 2];
            for (int i = 0; i < copyNodeArray.length; i++) {
                Node<K, V> temp = copyNodeArray[i];
                while (temp != null) {
                    put(temp.key, temp.value);
                    temp = temp.next;
                }
            }
        }
    }
}
