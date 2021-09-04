package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V newValue) {
            value = newValue;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            Node<K, V> o = (Node<K, V>) new Object();
            return ((o.key == this.key)
                    || (o.key != null
                    && o.key.equals(this.key)))
                    && ((o.value == this.value)
                    || (o.value != null
                    && o.value.equals(this.value)));
        }

        public int hashCode() {
            return key == null ? 0 : key.hashCode();
        }
    }

    static int hash(Object key) {
        return key == null ? 0 : 17 + 31 * key.hashCode();
    }

    private void transfer(Node<K, V>[] newTable) {
        for (Node<K, V> kvNode : table) {
            if (kvNode == null) {
                continue;
            }
            putNewTable(newTable, kvNode.getKey(), kvNode.getValue());
            Node<K, V> temporaryNode = kvNode;
            while (temporaryNode.next != null) {
                temporaryNode = temporaryNode.next;
                putNewTable(newTable, temporaryNode.getKey(), temporaryNode.getValue());
            }
        }
    }

    private void setNewValue(K key, V newValue) {
        for (Node<K, V> kvNode : table) {
            if (kvNode != null) {
                if (key == null && key == kvNode.key) {
                    kvNode.setValue(newValue);
                    return;
                }
                if (kvNode.key == null) {
                    continue;
                }
                if (kvNode.key.equals(key)) {
                    kvNode.setValue(newValue);
                    return;
                }
                Node<K, V> temporaryNode = kvNode;
                while (temporaryNode.next != null) {
                    if (temporaryNode.key.equals(key)) {
                        temporaryNode.setValue(newValue);
                        return;
                    }
                    temporaryNode = temporaryNode.next;
                }
                if (temporaryNode.key.equals(key)) {
                    temporaryNode.setValue(newValue);
                    return;
                }
            }
        }
    }

    private void putNewTable(Node<K, V>[] newTable, K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = Math.abs(newNode.hash) % newTable.length;
        if (newTable[index] != null) {
            Node<K, V> temporaryNode = newTable[index];
            while (temporaryNode.next != null) {
                temporaryNode = temporaryNode.next;
            }
            temporaryNode.next = newNode;
            return;
        }
        newTable[index] = newNode;
    }

    private Node<K, V>[] resizeNewTable() {
        return new Node[table.length * 2];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (key == null && table[0] != null) {
            if (newNode.key == table[0].key) {
                table[0].setValue(value);
                return;
            }
            Node<K, V> temporaryNode = table[0];
            while (temporaryNode.next != null) {
                if (temporaryNode.key == key) {
                    temporaryNode.setValue(value);
                    return;
                }
                temporaryNode = temporaryNode.next;
            }
            temporaryNode.next = newNode;
            size++;
            return;
        }
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] newTable = resizeNewTable();
            transfer(newTable);
            table = newTable;
        }
        if (getValue(key) != null) {
            setNewValue(key, value);
            return;
        }
        size++;
        if (key == null) {
            table[0] = newNode;
            return;
        }
        int index = Math.abs(newNode.hash) % table.length;
        if (table[index] != null) {
            Node<K, V> temporaryNode = table[index];
            if (temporaryNode.key != null && temporaryNode.key.equals(key)) {
                temporaryNode.setValue(value);
                size--;
                return;
            }
            while (temporaryNode.next != null) {
                if (temporaryNode.key == key
                        || (temporaryNode.key != null
                        && temporaryNode.key.equals(key))) {
                    temporaryNode.setValue(value);
                    size--;
                    return;
                }
                temporaryNode = temporaryNode.next;
            }
            temporaryNode.next = newNode;
            return;
        }
        table[index] = newNode;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> kvNode : table) {
            if (kvNode != null) {
                if (key == null && key == kvNode.key) {
                    return table[0].getValue();
                }
                if (kvNode.key == null) {
                    continue;
                }
                if (kvNode.key.equals(key)) {
                    return kvNode.getValue();
                }
                Node<K, V> temporaryNode = kvNode;
                if (temporaryNode.key.equals(key)) {
                    return temporaryNode.getValue();
                }
                while (temporaryNode.next != null) {
                    if (temporaryNode.key == key
                            || (temporaryNode.key != null
                            && temporaryNode.key.equals(key))) {
                        return temporaryNode.getValue();
                    }
                    temporaryNode = temporaryNode.next;
                }
                if (temporaryNode.key == key
                        || (temporaryNode.key != null
                        && temporaryNode.key.equals(key))) {
                    return temporaryNode.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
