package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] array;
    private int size;
    private int threshold;

    MyHashMap() {
    }

    @Override
    public void put(K key, V value) {
        if (mapIsEmpty()) {
            array = resize();
        }
        int tempHash = hash(key);
        if (array[tempHash] == null) {
            array[tempHash] = new Node<>(tempHash, key, value, null);
        } else {
            if (isEqual(key, array[tempHash].key)) {
                array[tempHash].value = value;
                return;
            }
            Node<K, V> tempNode = array[tempHash];
            while (tempNode.next != null) {
                if (isEqual(key, tempNode.next.key)) {
                    tempNode.next.value = value;
                    return;
                }
                tempNode = tempNode.next;
            }
            tempNode.next = new Node<>(tempHash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    private boolean isEqual(K keyOne, K keyTwo) {
        return (keyOne == keyTwo) || (keyOne != null && keyOne.equals(keyTwo));
    }


    private Node<K, V>[] resize() {
        if (size == 0) {
            array = new Node[INITIAL_CAPACITY];
            threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
            return array;
        }
       // capacity = array.length;
        Node<K, V>[] tempArray = array;
        array = (Node<K, V>[]) new Node[array.length * MULTIPLIER];
        threshold = (int) (array.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> tempNode : tempArray) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    private int hash(Object key) {
        int h;
        return Math.abs(((key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16)) % array.length);
    }

    @Override
    public V getValue(K key) {
        if (mapIsEmpty()) {
            return null;
        }
        int hashKey = hash(key);
        Node<K, V> tempNode = array[hashKey];
        if (isEqual(tempNode.key, key)) {
            return tempNode.value;
        }
        while (tempNode.next != null) {
            if (isEqual(tempNode.next.key, key)) {
                return tempNode.next.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    private boolean mapIsEmpty() {
        return array == null || array.length == 0;
    }


    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }


    }
}
/*
Хешмапа
1. Имеет поле Node[] - размером в 16 👌
a) Node имеет поле int hash  👌
2. Внутри метода put, есть метод который считает хеш ключа(правка в Б), который передается
подпункт по put, вызывает метод putVal(hash(key), key, value ... ) остальное вряд ли нужно 👌
а)   в случае null = 0 || key.hashcode() ^ >>> 16 (скорее всего просто делиться по остатку на 16) 👌
б) (!) Вычисляет не хеш, а место в массиве куда этот key можно закинуть. 👌
с) Пункт хеш понадобиться для get(key) (мы вычисляем по этому значению). 👌
3. Есть внутренний класс Node(Нагло скопировал) 👌
a) Создается нода, в которую мы помещаем ключ/value/hash по hash позиции. 👌
б) В случае put, если key с одинаковым хешем equals key с уже готовым ключем, второй теряется в небытие ;(
4. В методе put, вызываем метод putValue(hash, key, value) 👌
В ресайзе
1. Мы проверяем что основная таблица (array) в нулл или length == 0
В случае тру, мы инициализируем через resize() изничальную хешмапу. и где-то запоминаем длину(а надо?)
а) Метод ресайз возвращает массив нового.
Подпункты по ресайзу:
1) Проверяем старое значение capacity
В случае если capactity == 0 && old thr == 0 TODO
capacity = 16
newThr = load factor * 16
thr нужен, чтоб определиться когда нам нужно делать resize()
if(++size



 */