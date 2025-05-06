package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K,V>[] table;
    private int defaultCapacity = 16;
    private double loadFactor = 0.75d;
    private int size = 0;

    public MyHashMap() {
        table = new Node[defaultCapacity];
    }

    @Override
    public void put(K key, V value) {
        int bucketNumber = getBucketNumber(hash(key));
        Node<K,V> newNode = new Node<>(key, value, hash(key), null);
        if (table[bucketNumber] == null) {
            table[bucketNumber] = newNode;
        } else {
            Node<K,V> node = table[bucketNumber];
            if (node.next == null) {
                if ((newNode.key != null && newNode.key.equals(node.key))
                        || (newNode.key == null && node.key == null)) {
                    node.value = value;
                    return;
                } else {
                    node.next = newNode;
                }
            } else {
                do {
                    if ((newNode.key != null && newNode.key.equals(node.key))
                            || (newNode.key == null && node.key == null)) {
                        node.value = value;
                        return;
                    }
                    if (node.next == null) {
                        node.next = newNode;
                        break;
                    }
                } while ((node = node.next) != null);
            }
        }
        size++;
        isResizeNeeded();
    }

    @Override
    public V getValue(K key) {
        int bucketNumber = getBucketNumber(hash(key));
        if (table[bucketNumber] != null) {
            if (table[bucketNumber].next == null) {
                if ((key != null && key.equals(table[bucketNumber].key))
                        || (key == null && (table[bucketNumber].key == null))) {
                    return table[bucketNumber].value;
                }
            } else {
                Node<K,V> node = table[bucketNumber];
                do {
                    if ((key != null && key.equals(node.key))
                            || (key == null && node.key == null)) {
                        return node.value;
                    }
                } while ((node = node.next) != null);
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketNumber(int hash) {
        return hash % table.length;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    private void isResizeNeeded() {
        if (size >= table.length * loadFactor) {
            resize();
        }
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K,V> oldNode = oldTable[i];
                do {
                    Node<K,V> newNode = new Node<>(oldNode.key, oldNode.value,
                            hash(oldNode.key),null);
                    Node<K,V> node;
                    int bucketNumber = getBucketNumber(hash(newNode.key));
                    do {
                        if (table[bucketNumber] == null) {
                            table[bucketNumber] = newNode;
                        } else {
                            node = table[bucketNumber];
                            while (node.next != null) {
                                node = node.next;
                            }
                            node.next = newNode;
                        }
                    } while ((newNode = newNode.next) != null);
                } while ((oldNode = oldNode.next) != null);
            }
        }
    }

    private static class Node<K,V> {
        private int keyHash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, int keyHash, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.keyHash = keyHash;
            this.next = next;
        }
    }
}
