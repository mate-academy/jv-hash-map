package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CAPACITY = 16;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[START_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            int index = 0;
            if (table[index] == null) {
                table[index] = new Node<>(null, value);
            } else {
                Node<K, V> currentNode = table[index];
                while (currentNode != null) {
                    if (currentNode.key == null) {
                        currentNode.value = value;
                        return;
                    }
                    currentNode = currentNode.next;
                }
                Node<K, V> newNode = new Node<>(null, value);
                newNode.next = table[index];
                table[index] = newNode;
            }
            size++;
            if ((double) size / table.length > 0.75) {
                resize();
            }
            return;
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
        if ((double) size / table.length > 0.75) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return valueForNullKey();
        }
        return valueForKey(key);
    }

    @Override
    public int getSize() {
        return size;
    }

    final int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                putAfterResize(oldNode.key, oldNode.value, newIndex(oldNode.key, newCapacity));
                oldNode = oldNode.next;
            }
        }
    }

    private int newIndex(K key, int newCapacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % newCapacity;
    }

    private V valueForKey(K key) {
        Node<K, V> currentNode = table[getIndex(key)];
        while (currentNode != null) {
            if (currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private V valueForNullKey() {
        int hash = 0;
        Node<K, V> currentNode = table[hash];
        while (currentNode != null) {
            if (currentNode.key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void putAfterResize(K key, V value, int index) {
        if (key == null) {
            if (table[index] == null) {
                table[index] = new Node<>(null, value);
            } else {
                Node<K, V> currentNode = table[index];
                while (currentNode != null) {
                    if (currentNode.key == null) {
                        currentNode.value = value;
                        return;
                    }
                    currentNode = currentNode.next;
                }
                Node<K, V> newNode = new Node<>(null, value);
                newNode.next = table[index];
                table[index] = newNode;
            }
            size++;
            if ((double) size / table.length > 0.75) {
                resize();
            }
            return;
        }
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
        if ((double) size / table.length > 0.75) {
            resize();
        }
    }
}
