package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] hashTable = new Node[DEFAULT_CAPACITY];
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;

    private int getIndex(K key) {
        int hash = 31;
        hash = hash * 17 + key.hashCode();
        return key == null ? 0 : Math.abs(hash) % hashTable.length;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            if (hashTable[0] == null) {
                hashTable[0] = new Node<>(key, value, null);
                size++;
            } else if (hashTable[0].key == key) {
                hashTable[0].value = value;
            } else {
                Node<K, V> tempNode = hashTable[0];
                while (tempNode != null) {
                    if (tempNode.key == key || tempNode.key.equals(key)) {
                        tempNode.value = value;
                    }
                }
            }
            return;
        }
        if (size > hashTable.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        if (hashTable[index] == null) {
            hashTable[index] = new Node(key, value, null);
            size++;
        } else {
            Node<K, V> tempNode = hashTable[index];
            while (tempNode != null) {
                if (key == tempNode.key || key.equals(tempNode.key)) {
                    tempNode.value = value;
                    return;
                }
                tempNode = tempNode.linkToNextNode;
            }
            Node<K, V> newNode = new Node<>(key, value, hashTable[index]);
            hashTable[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) hashTable[0].value;
        }
        int index = getIndex(key);
        Node<K, V> newNode = hashTable[index];
        while (newNode != null) {
            if (key.equals(newNode.key)) {
                return newNode.value;
            }
            newNode = newNode.linkToNextNode;
        }
        return null;
    }

    public void resize() {
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[hashTable.length * 2];
        size = 0;
        for (int i = 0; i < oldHashTable.length; i++) {
            if (oldHashTable[i] == null) {
                continue;
            }
            Node<K, V> node = oldHashTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.linkToNextNode;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    class Node<K, V> {
        private Node<K, V> linkToNextNode;
        private K key;
        private V value;
        private int hash;

        public Node(K key, V value, Node<K, V> linkToNextNode) {
            this.key = key;
            this.value = value;
            this.linkToNextNode = linkToNextNode;
        }
    }
}
