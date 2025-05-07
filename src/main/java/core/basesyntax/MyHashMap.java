package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        if (key == null) {
            Node<K,V> node = table[0];
            if (node != null) {
                if (node.key == null && node.next == null) {
                    node.value = value;
                } else if (node.key != null && node.next == null) {
                    node.next = addNode(null,value);
                    size++;
                } else {
                    while (node.next != null) {
                        if (node.key == null) {
                            node.value = value;
                            return;
                        }
                        node = node.next;
                    }
                    node.next = addNode(null,value);
                    size++;
                }
            } else {
                table[0] = addNode(null,value);
                size++;
            }
        } else if (nodeBusyAndKeysEquals(key)) {
            table[bucketIndex(key)].value = value;
        } else if (nodeBusyAndKeysDifferent(key)) {
            if (noCollision(key)) {
                table[bucketIndex(key)].next = addNode(key,value);
                size++;
            } else {
                Node<K,V> current = table[bucketIndex(key)];
                while (current.next != null) {
                    if (current.next.key != null && current.next.key.equals(key)) {
                        current.next.value = value;
                        return;
                    }
                    current = current.next;
                }
                current.next = addNode(key,value);
                size++;
            }
        } else {
            table[bucketIndex(key)] = addNode(key,value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[bucketIndex(key)];
        if (node != null) {
            if (keysEquals(node,key)) {
                return node.value;
            } else {
                return searchForCollision(node, key);
            }
        }
        return null;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = (Node<K,V>[])new Node[table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean nodeBusyAndKeysEquals(K key) {
        return table[bucketIndex(key)] != null
                && table[bucketIndex(key)].key.equals(key);
    }

    private boolean keysEquals(Node<K,V> node, K key) {
        return (node.key == null && key == null) || node.key != null && node.key.equals(key);
    }

    private boolean nodeBusyAndKeysDifferent(K key) {
        return table[bucketIndex(key)] != null
                && !(table[bucketIndex(key)].key.equals(key));
    }

    private boolean noCollision(K key) {
        return table[bucketIndex(key)].next == null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucketIndex(K key) {
        int hash = key == null ? 0 : key.hashCode() % table.length;

        return Math.abs(hash);
    }

    private Node<K,V> addNode(K key, V value) {
        if (key == null) {
            return new Node<>(0, key, value,null);
        } else {
            return new Node<>(key.hashCode(), key, value,null);
        }
    }

    private V searchForCollision(Node<K,V> node, K key) {
        while (node != null) {
            if (node.key == null && key == null || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
