package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (table == null || size > threshold) {
            resizeTable();
        }
        if (key == null) {
            keyNullCase(value);
        } else if (nodeBusyAndKeysEquals(key)) {
            table[bucketIndex(key)].value = value;
        } else if (nodeBusyAndKeysDifferent(key)) {
            if (noCollision(key)) {
                table[bucketIndex(key)].next = addNode(key,value);
                size++;
            } else {
                putWithCollision(key,value);
            }
        } else {
            table[bucketIndex(key)] = addNode(key,value);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (size > 0) {
            Node<K,V> node = table[bucketIndex(key)];
            if (node != null) {
                if (keysEquals(node,key)) {
                    return node.value;
                } else {
                    return searchForCollision(node, key);
                }
            }
        }
        return null;
    }

    private void resizeTable() {
        Node<K,V>[] newTable;
        if (size > threshold) {
            capacity = capacity * 2;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            newTable = (Node<K,V>[])new MyHashMap.Node[capacity];
            reHashNodes(newTable);

        } else {
            makeNewEmptyTable();
        }
    }

    private void reHashNodes(Node<K,V>[] newTable) {
        for (Node<K, V> node : table) {
            if (node != null) {
                if (node.next == null) {
                    putInNewIndex(node,newTable);
                } else {
                    while (node != null) {
                        putInNewIndex(node,newTable);
                        node = node.next;
                    }
                }
            }
        }
        table = newTable;
    }

    private void makeNewEmptyTable() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K,V>[])new MyHashMap.Node[capacity];
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

    private void keyNullCase(V value) {
        if (table[0] != null) {
            table[0].value = value;
        } else {
            table[0] = new Node<>(0, null, value, null);
            size++;
        }
    }

    private boolean noCollision(K key) {
        return table[bucketIndex(key)].next == null;
    }

    private void putWithCollision(K key, V value) {
        Node<K,V> current = table[bucketIndex(key)].next;
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
            } else if (current.next == null) {
                current.next = addNode(key,value);
                size++;
                return;
            } else {
                current = current.next;
            }
        }
    }

    private void putInNewIndex(Node<K,V> kvNode, Node<K,V>[] newTable) {
        Node<K,V> node = newTable[bucketIndex(kvNode.key)];
        if (node == null) {
            node = addNode(kvNode.key, kvNode.value);
        } else if (node.next == null) {
            node.next = addNode(kvNode.key, kvNode.value);
        } else {
            node = newTable[bucketIndex(kvNode.key)].next;
            while (node != null) {
                if (node.next == null) {
                    node.next = addNode(kvNode.key, kvNode.value);
                    return;
                }
                node = node.next;
            }
        }
        newTable[bucketIndex(kvNode.key)] = node;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucketIndex(K key) {
        int hash = key == null ? 0 : key.hashCode() % capacity;
        return hash > 0 ? hash : -hash;
    }

    private Node<K,V> addNode(K key, V value) {
        return new Node<>(key.hashCode(), key, value,null);
    }

    private V searchForCollision(Node<K,V> node, K key) {
        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    private static class Node<K,V> {
        private int hashCode;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hashCode = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (object.getClass() == this.getClass()) {
                Node<K,V> nodeObject = (Node<K,V>) object;
                return (this.hashCode == nodeObject.hashCode)
                        && ((this.key != null && nodeObject.key != null)
                        ? (this.key.equals(nodeObject.key))
                        : (this.key == null & nodeObject.key == null))
                        && ((this.value != null && nodeObject.value != null)
                        ? (this.value.equals(nodeObject.value))
                        : (this.value == null && nodeObject.value == null))
                        && ((this.next != null && nodeObject.next != null)
                        ? this.next.equals(nodeObject.next)
                        : (this.next == null && nodeObject.next == null));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 17 * (this.key == null ? 0 : key.hashCode())
                    + (this.value == null ? 0 : value.hashCode())
                    + (this.next == null ? 0 : this.next.hashCode()) + hashCode;
        }
    }
}
