package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K,V>[] table;
    private int defaultCapacity = 16;
    private double loadFactor = 0.75d;
    private int size = 0;

    public MyHashMap() {
        table = new Node[defaultCapacity];
    }

    public MyHashMap(K key, V value) {
        table = new Node[defaultCapacity];
        put(key, value);
    }

    @Override
    public void put(K key, V value) {
        int bucketNumber = getBucketNumber(hash(key));
        Node<K,V> newNode = new Node<>(key, value, hash(key), null);
        if (table[bucketNumber] == null) {
            table[bucketNumber] = newNode;
            size++;
            resize();
        } else {
            Node<K,V> node = table[bucketNumber];
            if (node.next == null) {
                if ((newNode.key != null && newNode.key.equals(node.key))
                        || (newNode.key == null && node.key == null)) {
                    node.value = value;
                    return;
                }
                node.next = newNode;
                size++;
                resize();
            } else {
                do {
                    if ((newNode.key != null && newNode.key.equals(node.key))
                            || (newNode.key == null && node.key == null)) {
                        node.value = value;
                        return;
                    }
                    if (node.next == null) {
                        node.next = newNode;
                        size++;
                        resize();
                    }
                } while ((node = node.next) != null);
            }
        }

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

    private boolean isResizeNeeded() {
        return size >= table.length * loadFactor;
    }

    private void resize() {
        if (isResizeNeeded()) {
            Node<K,V>[] oldTable = table;
            Node<K,V>[] newTable = new Node[table.length * 2];
            size = 0;
            table = newTable;
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    Node<K,V> node = oldTable[i];
                    oldTable[i] = null;
                    do {
                        put(node.key, node.value);
                    } while ((node = node.next) != null);
                }
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

        public int getKeyHash() {
            return keyHash;
        }

        public void setKeyHash(int keyHash) {
            this.keyHash = keyHash;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
