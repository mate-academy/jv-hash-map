package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        resize();
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        final int index = getIndex(hash(key));

        if (table.length != 0) {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key == key || (current.key != null 
                        && current.key.equals(key))) {
                    return current.value;
                }
                current = current.next;      
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
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
        int oldCap = 0;
        int newCap;
        
        if (table == null) {
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
                    putVal(oldTable[i].hash, oldTable[i].key, oldTable[i].value);
                    if (oldTable[i].next != null) {
                        Node<K, V> current = oldTable[i];
                        while (current.next != null) {
                            current = current.next;
                            putVal(current.hash, current.key, current.value);
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

    private void putVal(int hash, K key, V value) {
        final int index = getIndex(hash);
        
        if (table[index] == null) {
            table[index] = new Node<K, V>(hash, key, value);
        } else {
            Node<K, V> current = table[index];
            while (true) {
                if (current.key == key || (current.key != null 
                        && current.key.equals(key))) {
                    current.value = value;
                    size--;
                    break;
                } 
                if (current.next == null) {
                    current.next = new Node<K, V>(hash, key, value);
                    break;
                }
                current = current.next;                   
            }
        }

        if (size > threshold) {
            resize();
        }
    }

    private int getIndex(int hash) {
        final int cap = table.length;
        return hash & cap - 1;
    }
}
