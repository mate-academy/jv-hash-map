package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INCREMENT = 2;
    private int size;
    private Node<K, V>[] nodeList;

    @Override
    public void put(K key, V value) {
        checkCapacity();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        if (nodeList == null) {
            return null;
        }
        int nodePosition = getIndex(key);
        var tempNode = nodeList[nodePosition];
        while (tempNode != null) {
            if ((key != null
                    && hash(tempNode.key) == hash(key)
                    && key.equals(tempNode.key))
                    || (key == null && tempNode.key == null)) {
                return tempNode.item;
            }
            if (tempNode.next != null) {
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapacity() {
        if (size == 0) {
            nodeList = new Node[DEFAULT_INITIAL_CAPACITY];
        }
        if (size == (int) (nodeList.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : hash(key) % nodeList.length;
    }

    private void putValue(K key, V value) {
        var newNode = new Node<>(key, value, null);
        int nodePosition = getIndex(newNode.key);
        if (nodeList[nodePosition] == null) {
            nodeList[nodePosition] = newNode;
        } else {
            var nextNode = nodeList[nodePosition].next;
            var tempNode = nodeList[nodePosition];
            while (tempNode != null) {
                if ((newNode.key != null
                        && hash(newNode.key) == hash(tempNode.key)
                        && newNode.key.equals(tempNode.key))
                        || (newNode.key == null && tempNode.key == null)) {
                    tempNode.item = newNode.item;
                    return;
                }
                if (nextNode == null) {
                    tempNode.next = newNode;
                    tempNode = newNode.next;
                } else {
                    tempNode = nextNode;
                    nextNode = nextNode.next;
                }
            }
        }
        size++;
    }

    private static class Node<K, V> {
        private final K key;
        private V item;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.item = value;
            this.next = next;
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        var oldNodeList = nodeList;
        nodeList = new Node[nodeList.length * DEFAULT_INCREMENT];
        size = 0;
        for (Node<K, V> node : oldNodeList) {
            Node<K, V> tempNode = node;
            while (tempNode != null) {
                putValue(tempNode.key, tempNode.item);
                tempNode = tempNode.next;
            }
        }
    }
}
