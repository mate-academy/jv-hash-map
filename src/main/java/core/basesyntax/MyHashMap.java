package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACYTY = 16;
    private static final double GROWE_INDEX = 0.75;
    private int size;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        hashMap = new Node[DEFAULT_CAPACYTY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = getHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        int index = hash % hashMap.length;
        if (hashMap[index] == null) {
            hashMap[index] = newNode;
            size++;
        } else {
            Node<K, V> node = hashMap[index];
            while (node.next != null) {
                if (equalsChek(node, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if (equalsChek(node, key)) {
                node.value = value;
            } else {
                node.next = newNode;
                size++;
            }
        }
        if ((double) size / hashMap.length >= GROWE_INDEX) {
            grow();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = hash % hashMap.length;
        Node<K, V> node = hashMap[index];
        while (node != null) {
            if (equalsChek(node, key)) {
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

    private int getHash(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        if (hash < 0) {
            hash = hash * -1;
        }
        return hash;
    }

    private void grow() {
        Node<K, V>[] oldHashMap = hashMap;
        hashMap = new Node[hashMap.length * 2];
        size = 0;
        for (Node<K, V> current : oldHashMap) {
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private boolean equalsChek(Node<K, V> node, K key) {
        return node.key == key || key != null && key.equals(node.key);
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
