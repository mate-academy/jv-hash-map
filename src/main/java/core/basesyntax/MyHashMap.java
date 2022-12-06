package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<K, V>(hash(key), key, value);
        putNode(newNode, false);
    }

    @Override
    public V getValue(K key) {
        if (table == null || table.length == 0) {
            return null;
        }
        final int cap = table.length;
        final int index = hash(key) & cap - 1;
        if (table[index] != null) {
            if (table[index].key == key || (table[index].key != null 
                    && table[index].key.equals(key))) {
                return table[index].value;
            } else {
                if (table[index].next != null) {
                    Node<K, V> current = table[index];
                    while (current.next != null) {
                        current = current.next;
                        if (current.key == key || (current.key != null 
                                && current.key.equals(key))) {
                            return current.value;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        final Node<K, V>[] oldTable = table;
        final boolean isResizing = true;
        int oldCap = 0;
        int newCap;
        
        if (oldTable == null || oldTable.length == 0) {
            newCap = DEFAULT_INITIAL_CAPACITY;
        } else {
            oldCap = oldTable.length;
            newCap = oldCap << 1;
        }
        threshold = (int)(newCap * DEFAULT_LOAD_FACTOR);
        final Node<K, V>[] newTable = (Node<K, V>[])new Node[newCap];
        table = newTable;

        if (oldCap != 0) {
            for (int i = 0; i < oldCap; i++) {
                if (oldTable[i] != null) {
                    putNode(new Node<K, V>(oldTable[i].hash, oldTable[i].key, 
                            oldTable[i].value), isResizing);
                    if (oldTable[i].next != null) {
                        Node<K, V> current = oldTable[i];
                        while (current.next != null) {
                            current = current.next;
                            putNode(new Node<K, V>(current.hash, current.key, 
                                    current.value), isResizing);
                        }
                    }
                }                                       
            }
        }
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void putNode(Node<K, V> node, boolean isResizing) {
        if (table == null || table.length == 0) {
            resize();
        }
        final int cap = table.length;
        final int index = node.hash & cap - 1;
        
        if (table[index] == null) {
            table[index] = node;
        } else {
            if (table[index].key == node.key || (table[index].key != null 
                    && table[index].key.equals(node.key))) {
                table[index].value = node.value;                
                size--;               
            } else {
                Node<K, V> current = table[index];
                while (current.next != null) {
                    current = current.next;
                    if (current.key == node.key || (current.key != null 
                            && current.key.equals(node.key))) {
                        current.value = node.value;
                        size--;
                        break;
                    }
                }
                if (current.next == null) {
                    current.next = node;
                }
            }
        }

        if (isResizing == false) {
            size++;
        }
        if (size > threshold) {
            resize();
        }
    }
}
