package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size = 0;

    MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= DEFAULT_LOAD_FACTOR * table.length) {
            resize();
        }
        int hashInputNode = (key == null) ? 0 : key.hashCode();
        Node<K, V> node = new Node<>(hashInputNode, key, value, null);
        int index = (hashInputNode == 0) ? 0 : Math.abs(hashInputNode) % table.length;
        Node<K, V> currentNode = table[index];
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            if (currentNode.hash == hashInputNode && (currentNode.key == key
                    || (currentNode.key != null && currentNode.key.equals(key)))) {
                currentNode.value = value;
            } else {
                for (; ; ) {
                    if (currentNode.hash == hashInputNode && (currentNode.key == key
                            || (currentNode.key != null && currentNode.key.equals(key)))) {
                        currentNode.value = value;
                        break;
                    }
                    if (currentNode.next == null) {
                        currentNode.next = node;
                        size++;
                        break;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hashInputNode = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(hashInputNode) % table.length;
        if (index < table.length) {
            Node<K, V> currentNode = table[index];
            while (true) {
                if (currentNode == null) {
                    return null;
                }
                if (currentNode.hash == hashInputNode && (currentNode.key == key
                        || (currentNode.key != null && currentNode.key.equals(key)))) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * 2];
        int newIndex;
        for (Node<K, V> node : table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                newIndex = Math.abs(currentNode.hash) % newTable.length;
                Node<K, V> newCurrentNode = newTable[newIndex];
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = new Node<>(currentNode.hash,
                            currentNode.key, currentNode.value, null);
                } else {
                    for (; ; ) {
                        if (newCurrentNode.next == null) {
                            newCurrentNode.next = new Node<>(currentNode.hash,
                                    currentNode.key, currentNode.value, null);
                            break;
                        }
                        newCurrentNode = newCurrentNode.next;
                    }
                }
                currentNode = currentNode.next;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
