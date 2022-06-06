package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final float COEFFICIENT = 0.75f;
    private static final int DOUBLING = 2;
    private int size;
    private int fullnessArray;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[CAPACITY];
        fullnessArray = (int) (CAPACITY * COEFFICIENT);
    }

    @Override
    public void put(K key, V value) {
        if (size == fullnessArray) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndexNode(key);
        Node<K, V> tmpNode = table[index];
        if (tmpNode == null) {
            table[index] = newNode;
            size++;
        } else {
            while (tmpNode != null) {
                if (keyCheck(key, tmpNode.key)) {
                    tmpNode.value = value;
                    break;
                }
                if (tmpNode.next == null) {
                    tmpNode.next = newNode;
                    size++;
                    break;
                }
                tmpNode = tmpNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tmpNode = table[getIndexNode(key)];
        while (tmpNode != null) {
            if (keyCheck(key, tmpNode.key)) {
                return tmpNode.value;
            }
            tmpNode = tmpNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean keyCheck(K key, K inputKey) {
        return (key == inputKey) || (inputKey != null && inputKey.equals(key));
    }

    private int getHash(K key) {
        return (key.hashCode() >>> CAPACITY);
    }

    private int getIndexNode(K key) {
        return key == null ? 0 : getHash(key) % table.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tmpNode = table;
        int newCapacity = tmpNode.length * DOUBLING;
        table = new Node[newCapacity];
        for (Node<K, V> node : tmpNode) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        fullnessArray = (int) (newCapacity * COEFFICIENT);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
