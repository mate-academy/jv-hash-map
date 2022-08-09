package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        Node<K, V>[] currentTable = table;
        int hash = hash(key);
        if (currentTable == null || currentTable.length == 0) {
            currentTable = resize();
        }
        if (currentTable[hash % currentTable.length] == null) {
            currentTable[hash % currentTable.length] = new Node<>(hash, key, value, null);
        } else {
            Node<K, V> currentNode = currentTable[hash % currentTable.length];
            if ((currentNode.key == null) && (key == null)) {
                currentNode.value = value;
                size--;
            } else {
                if (checkKeysEquals(currentNode, key)) {
                    currentNode.value = value;
                    size--;
                } else if (currentNode.next != null) {
                    while (currentNode.next != null) {
                        currentNode = currentNode.next;
                        if ((currentNode.key == null) && (key == null)) {
                            currentNode.value = value;
                            size--;
                            break;
                        } else {
                            if (checkKeysEquals(currentNode, key)) {
                                currentNode.value = value;
                                size--;
                                break;
                            }
                        }
                    }
                    if (currentNode.next == null) {
                        currentNode.next = new Node<>(hash, key, value, null);
                    }
                } else { //esli currentNode.next == 0
                    currentNode.next = new Node<>(hash, key, value, null);
                }
            }
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        if (table[getBucket(key)] == null) {
            return null;
        } else {
            Node<K, V> element = table[getBucket(key)];
            while (element != null) {
                if (checkKeysEquals(element,key)) {
                    return element.value;
                }
                element = element.next;
            }
            return element.value;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    private Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        Node<K, V>[] newTab;
        int oldCapacity;
        int newCapacity;
        int oldThreshold = threshold;
        int newThreshold;
        oldCapacity = (oldTab == null) ? 0 : oldTab.length;
        if (oldCapacity > 0) {
            newCapacity = oldCapacity * 2;
            newThreshold = oldThreshold * 2;
            newTab = new Node[newCapacity];
            newTab = transferTable(oldTab, newTab);
        } else {
            newCapacity = DEFAULT_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
            newTab = new Node[newCapacity];
        }
        threshold = newThreshold;
        table = newTab;
        return newTab;
    }

    private Node<K, V>[] transferTable(Node<K, V>[] oldTab, Node<K, V>[] newTab) {
        Node<K, V> oldElement;
        for (int i = 0; i < oldTab.length; i++) {
            oldElement = oldTab[i];
            if (oldElement == null) {
                continue;
            }
            transferElement(oldElement, newTab);
            while (oldElement.next != null) {
                oldElement = oldElement.next;
                transferElement(oldElement, newTab);
            }
        }
        return newTab;
    }

    private void transferElement(Node<K, V> element, Node<K, V>[] newTab) {
        int newPosition;
        Node<K, V> newElement;
        Node<K, V> oldElement;
        oldElement = element;
        newPosition = oldElement.hash % newTab.length;
        newElement = newTab[newPosition];
        if (newElement == null) {
            newElement = new Node<>(oldElement.hash, oldElement.key, oldElement.value, null);
            newTab[newPosition] = newElement;
        } else {
            while (newElement.next != null) {
                newElement = newElement.next;
            }
            newElement.next = new Node<>(oldElement.hash, oldElement.key, oldElement.value, null);
        }
    }

    private boolean checkKeysEquals(Node<K,V> currentNode, K key) {
        int hash = hash(key);
        return (currentNode.hash == hash)
                && ((currentNode.key == key)
                || (currentNode.key != null && currentNode.key.equals(key)));
    }

    private int getBucket(K key) {
        if (hash(key) == 0) {
            return 0;
        }
        return Math.abs(hash(key) % table.length);
    }

    private class Node<K, V> {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<K, V> node = (Node<K, V>) o;
            return (key == node.key) || ((key != null) && (key.equals(node.key)))
                    && (value == node.value)
                    || ((value != null) && (value.equals(node.value)));
        }

        @Override
        public int hashCode() {
            int result = 37;
            result = 37 * result + ((key == null) ? 0 : key.hashCode());
            result = 37 * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }
    }
}
