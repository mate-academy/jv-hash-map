package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = resize();
        }
        int index = (table.length - 1) & hash(key);
        Node<K, V> tempNode = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        } else {
            while (true) {
                if (tempNode.key == null ? key == null : tempNode.key.equals(key)) {
                    tempNode.value = value;
                    return;
                }
                if (tempNode.next == null) {
                    break;
                }
                tempNode = tempNode.next;
            }
            tempNode.next = new Node<>(hash(key), key, value, null);

        }

        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null && table.length > 0) {
            int index = (table.length - 1) & hash(key);
            Node<K, V> temp = table[index];
            while (temp != null) {
                if (temp.key == null ? key == null : temp.key.equals(key)) {
                    return temp.value;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private Node<K, V>[] resize() {
        final Node<K, V>[] oldTab = table;
        final int oldCap = table == null ? 0 : table.length;
        final int oldThr = threshold;
        int newCap;
        int newThr;
        if (oldCap > 0) {
            newCap = oldCap << 1;
            newThr = oldThr << 1;
        } else {
            newCap = INITIAL_CAPACITY;
            newThr = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        }

        threshold = newThr;
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int i = 0; i < oldTab.length; i++) {
                Node<K, V> tempNode = oldTab[i];
                if (tempNode != null) {
                    if (tempNode.next == null) {
                        table[(table.length - 1) & hash(tempNode.key)] = tempNode;
                    } else {
                        Node<K,V> loHead = null;
                        Node<K,V> loTail = null;
                        Node<K,V> hiHead = null;
                        Node<K,V> hiTail = null;
                        Node<K,V> next;
                        do {
                            next = tempNode.next;
                            if ((tempNode.hash & oldCap) == 0) {
                                if (loTail == null) {
                                    loHead = tempNode;
                                } else {
                                    loTail.next = tempNode;
                                }
                                loTail = tempNode;
                            } else {
                                if (hiTail == null) {
                                    hiHead = tempNode;
                                } else {
                                    hiTail.next = tempNode;
                                }
                                hiTail = tempNode;
                            }
                        } while ((tempNode = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[i + oldCap] = hiHead;
                        }
                    }
                }

            }
        }
        return table;
    }
}
