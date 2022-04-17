package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double loadFactor = 0.75;
    private int size;
    private int initialCapacity = 16;
    private Node<K, V>[] nodeArray;

    public MyHashMap() {
        nodeArray = new Node[initialCapacity];
    }

    class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;
    }

    @Override
    public void put(K key, V value) {
        Node node = new Node();
        if (key == null) {
            node.hash = 0;
        } else {
            node.hash = Math.abs(key.hashCode()) % initialCapacity;
        }
        node.key = key;
        node.value = value;
        node.next = null;
        for (int i = 0; i < initialCapacity; i++) {
            if (i == node.hash) {
                if (nodeArray[i] == null) {
                    nodeArray[i] = node;
                } else {
                    Node tempNode = nodeArray[i];
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
            }
        }
        size++;
        updateCapacity();
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < initialCapacity; i++) {
            Node temp = nodeArray[i];
            while (key == null) {
                if (key == temp.key) {
                    return (V) temp.value;
                }
                temp = temp.next;
            }
            while (temp != null) {
                if (key.equals(temp.key)) {
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

    public int updateCapacity() {
        if (size == initialCapacity * loadFactor + 1) {
            Node<K, V>[] copyNodeArray = nodeArray;
            nodeArray = new Node[initialCapacity * 2];
            for (int i = 0; i < copyNodeArray.length; i++) {
                nodeArray[i] = copyNodeArray[i];
            }
            return initialCapacity = initialCapacity * 2;
        }
        return initialCapacity;
    }
}
