package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_ARRAY_LENGTH = 16;
    private int size;
    private Node<K, V> [] values;

    public MyHashMap() {
        values = new Node[DEFAULT_ARRAY_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        if (values[getIndexFromKey(key)] == null) {
            Node<K, V> newNode = new Node<>(key, value, null);
            values[getIndexFromKey(key)] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = values[getIndexFromKey(key)];
            while (currentNode != null) {
                if (currentNode.nodeKey != null && currentNode.nodeKey.equals(key)
                        || currentNode.nodeKey == null && key == null) {
                    currentNode.nodeValue = value;
                    break;
                } else {
                    if (currentNode.next == null) {
                        currentNode.next = new Node<>(key,value,null);
                        size++;
                        break;
                    } else {
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        Node<K, V> currentNode = values[getIndexFromKey(key)];
        if (currentNode == null) {
            return null;
        }
        while (currentNode != null) {
            if (currentNode.nodeKey != null && currentNode.nodeKey.equals(key)
                    || currentNode.nodeKey == key) {
                return currentNode.nodeValue;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void arrayResize_Copy() {
        Node<K, V>[] oldArray = values;
        values = new Node[values.length * 2];
        size = 0;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null) {
                Node<K, V> currentNode = oldArray[i];
                do {
                    put(currentNode.nodeKey, currentNode.nodeValue);
                    currentNode = currentNode.next;
                } while (currentNode != null);
            }
        }
    }

    private int getIndexFromKey(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode() % values.length));
    }

    private void resizeIfNeeded() {
        if (size != 0 && size > (values.length * LOAD_FACTOR)) {
            arrayResize_Copy();
        }
    }

    private static class Node<K, V> {
        private V nodeValue;
        private final K nodeKey;
        private Node<K, V> next;

        Node(K nodeKey, V nodeValue, Node<K, V> next) {
            this.nodeKey = nodeKey;
            this.nodeValue = nodeValue;
            this.next = next;
        }
    }
}

