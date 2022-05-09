package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_LENGTH];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucketIndex = getHashByKey(key);
        Node<K, V> currentNode = table[bucketIndex];

        if (currentNode == null) {
            table[bucketIndex] = new Node<>(key, value, null);
        } else {
            while (currentNode != null) {
                if (key == currentNode.key
                        || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getHashByKey(key)];
        while (currentNode != null) {
            if (key == currentNode.key
                    || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        //return 0;
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tableForIncrease = table;
        table = new Node[tableForIncrease.length * 2];
        threshold = (int) ((tableForIncrease.length * 2) * LOAD_FACTOR);
        for (Node<K, V> node : tableForIncrease) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getHashByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
