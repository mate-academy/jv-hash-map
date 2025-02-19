package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCons = 16;
    private final double factory = 0.75;
    private int oldCap;
    private int newCap = 16;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        putVal(key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }

        int x = (key == null ? 0 : Math.abs(key.hashCode() % newCap));
        Node<K, V> newNode = table[x];

        while (newNode != null) {
            if ((key == null && newNode.key == null)
                    || (key != null && key.equals(newNode.key))) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putVal(K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        }
        if (size + 1 > threshold) {
            resize();
        }
        int x = key == null ? 0 : Math.abs(key.hashCode() % newCap);
        if (table[x] == null) {
            table[x] = new Node<>(key, value, x);
            size++;
        } else {
            Node<K, V> newNode = table[x];
            while (newNode.next != null) {
                if (newNode.key == null && key == null
                        || (key != null && key.equals(newNode.key))) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    break;
                }
                newNode = newNode.next;
            }
            if (newNode.key == null && key == null
                    || (key != null && key.equals(newNode.key))) {
                newNode.value = value;
                return;
            }
            newNode.next = new Node<>(key, value, x);
            size++;
        }

    }

    private class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next = null;

        private Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    private void resize() {
        oldCap = table == null ? 0 : table.length;
        newCap = oldCap == 0 ? defaultCons : oldCap * 2;
        threshold = (int) (factory * newCap);

        Node<K, V>[] oldTable = table;
        table = new Node[newCap];

        if (oldTable != null) {
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    Node<K, V> nextNode = node.next; // Сохраняем ссылку на следующий элемент
                    int x = node.key == null ? 0 : Math.abs(node.key.hashCode() % newCap);
                    node.next = table[x]; // Вставляем в новый массив, сохраняя цепочку
                    table[x] = node;
                    node = nextNode; // Переходим к следующему узлу
                }
            }
        }
    }

    private void transfer(int newCap) {
        Node<K, V>[] oldNode = table;
        table = new Node[newCap];
        size = 0;
        for (int i = 0; i < oldNode.length; i++) {
            if (oldNode[i] != null) {
                put(oldNode[i].key, oldNode[i].value);
                Node<K, V> node = oldNode[i];
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

}
